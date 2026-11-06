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
            System.out.println("\nPersist auto-author ...");
            bookstoreService.persistAutoAuthor();
            
            System.out.println("\nPersist identity-author ...");
            bookstoreService.persistIdentityAuthor();
            
            System.out.println("\nPersist assigned-author ...");
            bookstoreService.persistAssignedAuthor();
            
            System.out.println("\nPersist persistable-author ...");
            bookstoreService.persistPersistableAuthor();
            
            System.out.println("\nPersist version-author ...");
            bookstoreService.persistVersionAuthor();
        };
    }
}
