package com.billing;

import com.billing.entity.Invoice;
import com.billing.service.BillingService;
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

@SpringBootApplication
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    private final BillingService billingService;

    public MainApplication(BillingService billingService) {
        this.billingService = billingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
                      
            ExecutorService executor = Executors.newFixedThreadPool(3);

            // reader 1, starts and acquires PESSIMISTIC_READ
            executor.submit(() -> {
                log.info("[Reader 1] Generating invoice...");
                Invoice inv = billingService.generateInvoice(1L, "RO", new BigDecimal("100.00"));
                log.info("[Reader 1] Finished! Total: {}", inv.getTotalAmount());
            });

            // reader 2, starts at the same time and is NOT blocked by reader 1
            executor.submit(() -> {
                log.info("[Reader 2] Generating invoice...");
                Invoice inv = billingService.generateInvoice(2L, "RO", new BigDecimal("200.00"));
                log.info("[Reader 2] Finished! Total: {}", inv.getTotalAmount());
            });

            // brief pause so readers acquire their shared locks first
            Thread.sleep(200);

            // writer (admin), tries to update tax rate to 21% but is BLOCKED until both readers commit
            executor.submit(() -> {
                log.info("[Admin Writer] Attempting to update tax rate to 21%...");
                billingService.updateTaxRate("RO", new BigDecimal("0.21"));
                log.info("[Admin Writer] Tax rate updated successfully!");
            });

            executor.shutdown();
            executor.awaitTermination(15, TimeUnit.SECONDS);
        };
    }
}