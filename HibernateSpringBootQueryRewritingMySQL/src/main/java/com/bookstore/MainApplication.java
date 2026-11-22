package com.bookstore;

import com.bookstore.entity.Book;
import com.bookstore.rewriter.FullTextQueryRewriter;
import com.bookstore.service.BookstoreService;
import java.util.List;
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
            // AGAINST(:keyword IN BOOLEAN MODE)
            List<Book> booelanResult = bookstoreService.searchBooks(
                    "+spring -hibernate", FullTextQueryRewriter.SearchMode.BOOLEAN);
            System.out.println(booelanResult);

            // AGAINST(:keyword IN NATURAL LANGUAGE MODE)
            List<Book> naturalResult = bookstoreService.searchBooks(
                    "spring jpa tutorial", FullTextQueryRewriter.SearchMode.NATURAL_LANGUAGE);
            System.out.println(naturalResult);

            System.out.println("Done ..............");
        };
    }
}
