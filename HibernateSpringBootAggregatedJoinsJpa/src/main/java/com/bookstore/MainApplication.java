package com.bookstore;

import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
            bookstoreService.insertTestData();
            
            long start = System.nanoTime();
            
            List<Author> authors = bookstoreService.fetchAuthorsBooksTagsPublishersReviewersReviews();
            
            long end = System.nanoTime();
                       
            long durationInMs = TimeUnit.NANOSECONDS.toMillis((end-start));
            
            System.out.println("Query time: " + durationInMs + " ms");
        };
    }
}
