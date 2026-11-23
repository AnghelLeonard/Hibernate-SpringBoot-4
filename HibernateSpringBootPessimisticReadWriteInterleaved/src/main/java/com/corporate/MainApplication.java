package com.corporate;

import com.corporate.service.ExpenseService;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.PessimisticLockingFailureException;

@SpringBootApplication
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    private final ExpenseService expenseService;

    public MainApplication(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            ExecutorService executor = Executors.newFixedThreadPool(3);

            // task 1 - Alice's 1st top-up ($300)
            executor.submit(() -> runSafely(() -> expenseService.topUpWallet("alice", new BigDecimal("300.00"))));

            // task 2 - Alice's 2nd top-up ($300) at the same time
            executor.submit(() -> runSafely(() -> expenseService.topUpWallet("alice", new BigDecimal("300.00"))));

            // give tasks 1 and 2 200ms to obtain PESSIMISTIC_READ on 'ENG'
            Thread.sleep(200);

            // task 3, manager tries to lower 'ENG' limit from $500 -> $200 while tasks 1 and 2 are running
            executor.submit(() -> runSafely(() -> expenseService.updateDepartmentLimit(
                    "ENG", new BigDecimal("200.00"))));

            executor.shutdown();
            executor.awaitTermination(15, TimeUnit.SECONDS);
        };
    }

    private void runSafely(Runnable task) {
        try {
            task.run();
        } catch (IllegalStateException | IllegalArgumentException | PessimisticLockingFailureException e) {
            log.warn("[{}] Operation rejected: {}", Thread.currentThread().getName(), e.getMessage());
        }
    }
}
