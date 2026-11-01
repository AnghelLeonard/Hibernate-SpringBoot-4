package com.citylots.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class JdbcBatcher {

    private static final String SQL_INSERT = "INSERT INTO lots (lot) VALUES (?::jsonb)";

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final ExecutorService dbThreadPool;

    public JdbcBatcher(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;

        // bounded thread pool for batching
        this.dbThreadPool = new ThreadPoolExecutor(
                4,
                8, // keep maximum threads strictly below your HikariCP max pool size
                30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(50),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public record BatchResult(int rowsAffected, boolean isSuccess, Throwable error) {}

    public CompletableFuture<List<BatchResult>> executeParallelBatch(List<List<String>> jsonLists) {

        if (jsonLists == null || jsonLists.isEmpty()) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }

        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        
        // map chunks into CompletableFutures executing inside our custom thread pool
        List<CompletableFuture<BatchResult>> futures = jsonLists.stream()
                .map(chunk -> CompletableFuture.supplyAsync(
                        () -> executeEachSubBatch(chunk), dbThreadPool))
                .toList();

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
                );
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
