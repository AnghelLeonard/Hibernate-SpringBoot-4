package com.bookstore;

import com.bookstore.service.BookstoreService;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final BookstoreService bookstoreService;

    public MainApplication(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            Long authorId = 1L; // Olivia Goy from your INSERT script

            System.out.println("\nHigh-frequency operational update (sellRank):");
            bookstoreService.updateAuthorSellRank(authorId, 7);

            System.out.println("\nRecording a book sale (totalBooksSold):");
            bookstoreService.recordBookSale(authorId, 150L);

            System.out.println("\nConcurrent non-conflicting updates on the SAME row:");
            CompletableFuture<Void> rankUpdateTask = CompletableFuture.runAsync(()
                    -> bookstoreService.updateAuthorSellRank(authorId, 3)
            );

            CompletableFuture<Void> bioUpdateTask = CompletableFuture.runAsync(()
                    -> bookstoreService.updateAuthorBiography(
                            authorId,
                            "Updated biography: Award-winning horror and thriller author.",
                            Map.of(
                                    "website", "https://example.com",
                                    "twitter", "@oliviagoy_books",
                                    "newsletterSubscribers", 52000
                            )
                    )
            );

            // wait for both transactions to commit, neither throws OptimisticLockException!
            CompletableFuture.allOf(rankUpdateTask, bioUpdateTask).join();
            System.out.println("\nBoth concurrent transactions succeeded!");
        };
    }
}
