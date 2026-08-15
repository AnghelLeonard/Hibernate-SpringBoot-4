package com.bookstore;

import com.bookstore.service.BookstoreService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {
    
    // This application ends uop with the following exception:
    // jakarta.validation.ConstraintViolationException: Validation failed for 
    // classes [com.bookstore.entity.Review] during persist time for groups 
    // [jakarta.validation.groups.Default, ]

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
            bookstoreService.persistReviewOk();
            bookstoreService.persistReviewWrong();
        };
    }
}
