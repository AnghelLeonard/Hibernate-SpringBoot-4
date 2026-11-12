package com.app.service;

import com.app.repository.NumberRepository;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class PoolStressTestService {

    private final NumberRepository numberRepository;

    public PoolStressTestService(NumberRepository numberRepository) {
        this.numberRepository = numberRepository;
    }

    public void runConcurrentInserts(int taskCount) {
        try (var executor = Executors.newFixedThreadPool(15)) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    numberRepository.insertRandomNumber();
                    // simulate work outside the database transaction
                    simulateWork(); 
                });
            }
        }
    }

    private void simulateWork() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

