package com.bookstore.impl;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.StructuredTaskScope.Joiner;
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
        executeBatch(entities);
        timer.stop();

        logger.info(() -> "\nBatch time: " + timer.getTotalTimeMillis()
                + " ms (" + timer.getTotalTimeSeconds() + " s)");
    }

    public <S extends T> void executeBatch(List<S> list) throws InterruptedException {

        int pos = 0;
        try (var scope = open(Joiner.<Object>allSuccessfulOrThrow())) {

            while ((pos + batchSize) < list.size()) {
                int cpos = pos;
                scope.fork(() -> {
                    // if (dbConn.getQueueLength() >= 8) {
                    //    logger.info("Sorry, no connections left ...");
                    // } 
                    try {
                        dbConn.acquire();
                    } catch (InterruptedException ex) {
                    }
                    try {
                        batch(list.subList(cpos, cpos + batchSize));
                    } finally {
                        dbConn.release();
                    }                    
                });
                pos = pos + batchSize;
            }

            // batch the remaining entities
            int cpos = pos;
            scope.fork(() -> batch((List<S>) list.subList(cpos, list.size())));

            scope.join(); // Join subtasks, propagating exceptions
        }
    }

    public <S extends T> void batch(List<S> list) {

        txTemplate.executeWithoutResult(status -> {
            for (S entity : list) {
                entityManager.persist(entity);
            }
        });
    }
}
