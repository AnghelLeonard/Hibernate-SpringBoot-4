package com.bookstore;

import com.bookstore.service.BookstoreService;
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
                        
            System.out.println("\n\nInsert a new order and book:");
            bookstoreService.insertOrderWithBookLine("Mia Kola", "9780134685991", 2, 45.99);
            
            System.out.println("\n\nAdd a book to an exiting order:");
            bookstoreService.addBookToExistingOrder(1L, "9780132350884", 3, 42.00);
            
            System.out.println("\n\nRemoving a single book:");
            bookstoreService.removeBookFromOrder(2L, "9780132350884");
            
            System.out.println("\n\nRemoving an order:");
            bookstoreService.removeWholeOrder(1L);
            
            System.out.println("\n\nFetch order with books:");        
            bookstoreService.fetchOrderWithBooks(2L);
            bookstoreService.fetchOrderWithBooks(3L);
            
        };
    }
}
