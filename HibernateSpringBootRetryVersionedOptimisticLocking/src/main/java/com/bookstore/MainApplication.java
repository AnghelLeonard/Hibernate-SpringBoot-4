package com.bookstore;

import com.bookstore.entity.Inventory;
import com.bookstore.repository.InventoryRepository;
import com.bookstore.service.OrderPlacementService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableResilientMethods
@EnableScheduling
public class MainApplication {

    private final InventoryRepository inventoryRepository;
    private final OrderPlacementService orderPlacementService;

    public MainApplication(InventoryRepository inventoryRepository,
            OrderPlacementService orderPlacementService) {
        this.inventoryRepository = inventoryRepository;
        this.orderPlacementService = orderPlacementService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {           

            // simulate 5 concurrent buyers trying to buy 2 books each 
            // (only 5 in stock -> 2 succeed, 3 get OutOfStock)
            try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 1; i <= 5; i++) {
                    final int buyerNum = i;
                    executor.submit(() -> {
                        try {
                            var res = orderPlacementService.placeOrder("req-key-" + buyerNum, "buyer" + buyerNum + "@test.com", "BOOK-978-0134685991", 2);
                            System.out.println("Buyer " + buyerNum + " -> HTTP " + res.statusCode() + " | " + res.jsonBody());
                        } catch (Exception e) {
                            System.out.println("Buyer " + buyerNum + " -> FAILED: " + e.getMessage());
                        }
                    });
                }
            }

            // replay Buyer 1's exact request -> returns HTTP 200 (replayed=true) without touching stock
            var replay = orderPlacementService.placeOrder("req-key-1", "buyer1@test.com", "BOOK-978-0134685991", 2);
            System.out.println("Replay Buyer 1 -> HTTP " + replay.statusCode() + " (replayed=" + replay.replayed() + ")");
        };
    }
}
