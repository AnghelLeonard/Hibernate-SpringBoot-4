package com.citylots.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

@Component
public class JdbcBatcher {

    private static final Logger logger = Logger.getLogger(JdbcBatcher.class.getName());

    private static final String SQL_INSERT = "INSERT INTO lots (lot) VALUES (?::jsonb)";

    private static final Semaphore dbConn = new Semaphore(8, true); // we can have maximum 8 connections in use

    @Value("${jdbc.batch.size}")
    private int batchSize;

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public JdbcBatcher(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public record BatchResult(int rowsAffected, boolean isSuccess, Throwable error) {}

    public List<BatchResult> executeParallelBatch(List<String> jsonList)
            throws InterruptedException, ExecutionException {

        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        StopWatch timer = new StopWatch();
        timer.start();
        List<BatchResult> results = executeBatch(jsonList);
        timer.stop();

        logger.info(() -> "\nBatch time: " + timer.getTotalTimeMillis()
                + " ms (" + timer.getTotalTimeSeconds() + " s");
        
        return results;
    }

    private List<BatchResult> executeBatch(List<String> jsonList) throws InterruptedException {

        int pos = 0;

        // keep a list of all subtask handlers to inspect later
        List<Subtask<BatchResult>> subtasks = new ArrayList<>();

        try (var scope = open(Joiner.<BatchResult>allSuccessfulOrThrow())) {

            while ((pos + batchSize) < jsonList.size()) {
                int cpos = pos;
                Subtask<BatchResult> subtask = scope.fork(() -> {

                    dbConn.acquire();

                    try {
                        return executeEachSubBatch(jsonList.subList(cpos, cpos + batchSize));
                    } finally {
                        dbConn.release();
                    }
                });
                subtasks.add(subtask);
                pos = pos + batchSize;
            }

            // batch the remaining entities
            if (pos < jsonList.size()) {
                int cpos = pos;
                Subtask<BatchResult> subtask = scope.fork(() -> {
                    dbConn.acquire();
                    try {
                        return executeEachSubBatch((List<String>) jsonList.subList(cpos, jsonList.size()));
                    } finally {
                        dbConn.release();
                    }
                });
                subtasks.add(subtask);
            }

            scope.join(); // Join subtasks, propagating exceptions

            return subtasks.stream()
                    .map(Subtask::get)
                    .toList();
        }
    }

    private BatchResult executeEachSubBatch(List<String> jsonList) {

        try {
            int rowsUpdated = transactionTemplate.execute(status -> {
                int[] updateCounts = jdbcTemplate.batchUpdate(SQL_INSERT,
                        new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement pStmt, int i)
                            throws SQLException {
                        String jsonLine = jsonList.get(i);
                        pStmt.setString(1, jsonLine);
                    }

                    @Override
                    public int getBatchSize() {
                        return jsonList.size();
                    }
                });

                // sum up rows affected in this batch
                int totalRowsInBatch = 0;
                for (int count : updateCounts) {
                    if (count >= 0) {
                        totalRowsInBatch += count;
                    } else if (count == PreparedStatement.SUCCESS_NO_INFO) {
                        totalRowsInBatch++; // row updated successfully, but no count info available
                    }
                }

                return totalRowsInBatch;
            });

            return new BatchResult(rowsUpdated, true, null);

        } catch (TransactionException e) {
            return new BatchResult(0, false, e);
        }
    }
}
