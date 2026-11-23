package com.flashsale;

import com.flashsale.service.OrderService;
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
    
    private final OrderService orderService;

    public MainApplication(OrderService orderService) {
        this.orderService = orderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable buyTask = () -> {
            String thread = Thread.currentThread().getName();
            try {
                log.info("[{}] Attempting to buy sneaker...", thread);
                orderService.purchaseProduct(1L, 1);
                log.info("[{}] Purchase SUCCESS!", thread);
            } catch (IllegalStateException | IllegalArgumentException | PessimisticLockingFailureException e) {
                log.warn("[{}] Purchase FAILED: {}", thread, e.getMessage());
            }
        };

        // Fire two simultaneous purchases for the single remaining item
        executor.submit(buyTask);
        executor.submit(buyTask);

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);                        
        };
    }
}
