package com.bookstore.impl;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

@Component
public class BatchExecutor<T> {

    private static final Logger logger = Logger.getLogger(BatchExecutor.class.getName());

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final TransactionTemplate txTemplate;
    private final EntityManager entityManager;

    // uses all available processors
    // private static final ExecutorService executor = Executors.newFixedThreadPool(
    //            Runtime.getRuntime().availableProcessors());
    private static final ExecutorService executor = new ThreadPoolExecutor(
            4, // core threads
            8, // max threads (keep below HikariCP pool max)
            30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(50), // bound queue size to prevent OOM
            new ThreadPoolExecutor.CallerRunsPolicy() // fallback pressure strategy
    );

    public BatchExecutor(TransactionTemplate txTemplate, EntityManager entityManager) {
        this.txTemplate = txTemplate;
        this.entityManager = entityManager;
    }

    public List<T> saveInBatch(List<T> entities)
            throws InterruptedException, ExecutionException {

        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        final AtomicInteger count = new AtomicInteger();
        // map to a typed List of futures containing lists of entities
        List<CompletableFuture<List<T>>> futures = entities.stream()
                .collect(Collectors.groupingBy(c -> count.getAndIncrement() / batchSize))
                .values()
                .stream()
                .map(list -> executeBatch(list)) // each future resolves to a List<T>
                .toList();

        StopWatch timer = new StopWatch();
        timer.start();

        // wait for all batches to finish and flatten the list of lists into a single list
        CompletableFuture<List<T>> cfList = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.stream()
                .map(CompletableFuture::join) // safe to call join here; all are done
                .flatMap(List::stream)
                .collect(Collectors.toList()));

        cfList.get();
        timer.stop();

        logger.info(() -> "\nTotal batch time: " + timer.getTotalTimeMillis()
                + " ms (" + timer.getTotalTimeSeconds() + " s)");

        return cfList.resultNow(); // there is no need to wait 
    }

    public <S extends T> CompletableFuture<List<S>> executeBatch(List<S> list) {

        return CompletableFuture.supplyAsync(() -> {
            txTemplate.executeWithoutResult((TransactionStatus status) -> {
                for (S entity : list) {                    
                    entityManager.persist(entity);
                }
            });
            return list;
        }, executor);
    }
}
