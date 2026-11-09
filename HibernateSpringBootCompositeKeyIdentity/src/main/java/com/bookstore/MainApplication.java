package com.bookstore;

import com.bookstore.entity.Book;
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
            System.out.println("\nInsert new book:");
            Long generatedBookNumber = bookstoreService.insertBook();
            
            System.out.println("\nFetch book by composite key:");
            Book book = bookstoreService.fetchBook(generatedBookNumber);
            System.out.println(book);
            
            System.out.println("\nFetch book by publihser id:");
            List<Book> books = bookstoreService.fetchBookByPublisherId();
            System.out.println(books);
        };
    }
}
