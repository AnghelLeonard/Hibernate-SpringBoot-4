package com.invoice;

import com.invoice.service.InvoiceService;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SpringBootApplication
public class MainApplication {

    private final InvoiceService invoiceService;

    public MainApplication(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            CountDownLatch startGate = new CountDownLatch(1);

            // call 1 - add $700.00 Laptop (ID: 101)
            CompletableFuture<Void> requestA = CompletableFuture.runAsync(() -> {
                await(startGate);
                try {
                    invoiceService.addItem(42L, 101L, new BigDecimal("700.00"), "Dell XPS Laptop");
                    System.out.println("Request A succeeded!");
                } catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("Request A failed with Optimistic Lock conflict!");
                }
            });

            // call 2 - add $600.00 Monitor (ID: 102) at the exact same time
            CompletableFuture<Void> requestB = CompletableFuture.runAsync(() -> {
                await(startGate);
                try {
                    invoiceService.addItem(42L, 102L, new BigDecimal("600.00"), "4K Monitor");
                    System.out.println("Request B succeeded!");
                } catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("Request B failed with Optimistic Lock conflict!");
                }
            });

            // release both calls simultaneously
            startGate.countDown();
            CompletableFuture.allOf(requestA, requestB).join();
        };
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
