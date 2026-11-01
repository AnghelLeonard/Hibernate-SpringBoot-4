package com.bookstore.impl;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import static java.util.concurrent.StructuredTaskScope.open;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

@Component
public class BatchExecutor<T> {

    private static final Logger logger = Logger.getLogger(BatchExecutor.class.getName());

    private static final Semaphore dbConn = new Semaphore(8, true); // we can have maximum 8 connections in use

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final TransactionTemplate txTemplate;
    private final EntityManager entityManager;

    public BatchExecutor(TransactionTemplate txTemplate, EntityManager entityManager) {
        this.txTemplate = txTemplate;
        this.entityManager = entityManager;
    }

    public <S extends T> void saveInBatch(List<S> entities)
            throws InterruptedException, ExecutionException {

        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        StopWatch timer = new StopWatch();
        timer.start();
        int totalRows = executeBatch(entities);
        timer.stop();

        logger.info(() -> "\nBatch time: " + timer.getTotalTimeMillis()
                + " ms (" + timer.getTotalTimeSeconds() + " s) of " + totalRows + " rows");
    }

    public <S extends T> int executeBatch(List<S> list) throws InterruptedException {

        int pos = 0;

        // keep a list of all subtask handlers to inspect later
        List<Subtask<Integer>> subtasks = new ArrayList<>();

        try (var scope = open(Joiner.<Integer>allSuccessfulOrThrow())) {

            while ((pos + batchSize) < list.size()) {
                int cpos = pos;
                Subtask<Integer> subtask = scope.fork(() -> {

                    dbConn.acquire();

                    try {
                        return batch(list.subList(cpos, cpos + batchSize));
                    } finally {
                        dbConn.release();
                    }
                });
                subtasks.add(subtask);
                pos = pos + batchSize;
            }

            // batch the remaining entities
            if (pos < list.size()) {
                int cpos = pos;
                Subtask<Integer> subtask = scope.fork(() -> {
                    dbConn.acquire();
                    try {
                        return batch((List<S>) list.subList(cpos, list.size()));
                    } finally {
                        dbConn.release();
                    }
                });
                subtasks.add(subtask);
            }

            scope.join(); // Join subtasks, propagating exceptions

            return subtasks.stream()
                    .mapToInt(Subtask::get)
                    .sum();
        }
    }

    public <S extends T> int batch(List<S> list) {

        txTemplate.executeWithoutResult(status -> {
            for (S entity : list) {
                entityManager.persist(entity);
            }
        });

        return list.size();
    }
}
