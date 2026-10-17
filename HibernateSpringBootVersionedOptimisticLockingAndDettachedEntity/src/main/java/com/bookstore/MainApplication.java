package com.bookstore;

import com.bookstore.service.BookstoreService;
import com.bookstore.entity.Inventory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    // Running the application should result in 
    // org.springframework.orm.ObjectOptimisticLockingFailureException
    
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

            System.out.println("Triggering the first transaction ...");
            Inventory firstInventory = bookstoreService.firstTransactionFetchesAndReturn();
            System.out.println("First transaction committed successfully ..."); 
            
            System.out.println("Triggering the second transaction ...");
            bookstoreService.secondTransactionFetchesAndReturn();
            System.out.println("Second transaction committed successfully ..."); 
            
            // AT THIS POINT, THE firstInventory IS DETACHED
            firstInventory.setQuantity(firstInventory.getQuantity() - 1);
            
            System.out.println("Triggering the third transaction ...");
            bookstoreService.thirdTransactionMergesAndUpdates(firstInventory);
            System.out.println("Third transaction committed successfully ..."); 
        };
    }
}
