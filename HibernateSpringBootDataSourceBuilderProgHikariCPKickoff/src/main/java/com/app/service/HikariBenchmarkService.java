package com.app.service;

import com.app.repository.NumberRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HikariBenchmarkService {

    private static final Logger logger = LoggerFactory.getLogger(HikariBenchmarkService.class);
    private final NumberRepository numberRepository;

    public HikariBenchmarkService(NumberRepository numberRepository) {
        this.numberRepository = numberRepository;
    }

    public void runSpikeTest(int concurrentUsers, int requestsPerUser) throws InterruptedException {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(concurrentUsers);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger timeoutErrors = new AtomicInteger();

        long startTime = System.nanoTime();

        try (var executor = Executors.newFixedThreadPool(concurrentUsers)) {
            for (int i = 0; i < concurrentUsers; i++) {
                executor.submit(() -> {
                    try {
                        startGate.await(); // wait for simultaneous burst
                        for (int j = 0; j < requestsPerUser; j++) {
                            numberRepository.insertRandomNumber();
                            successCount.incrementAndGet();
                            
                            // simulate app/network processing time OUTSIDE the database connection
                            Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
                        }
                    } catch (InterruptedException e) {
                        timeoutErrors.incrementAndGet();
                        logger.warn("Pool acquisition failed: {}", e.getMessage());
                    } finally {
                        endGate.countDown();
                    }
                });
            }

            startGate.countDown(); // release all threads simultaneously
            endGate.await();       // wait for all threads to finish
        }

        long durationMs = (System.nanoTime() - startTime) / 1_000_000;
        logger.info("Benchmark finished in {} ms | Success: {} | Timeouts/Errors: {}",
                durationMs, successCount.get(), timeoutErrors.get());
    }
}

