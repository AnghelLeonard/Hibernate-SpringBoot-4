package com.bookstore;

import com.bookstore.projection.AuthorNameAge;
import java.util.List;
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
            
            displayAuthors(bookstoreService.fetchByGenre1());
            displayAuthors(bookstoreService.fetchByGenre2());
            displayAuthors(bookstoreService.fetchByGenre3());
            displayAuthors(bookstoreService.fetchByGenre4());
            displayAuthors(bookstoreService.fetchByGenre5());
        };
    }

    public void displayAuthors(List<AuthorNameAge> authors) {

        System.out.println("Number of authors:" + authors.size());

        for (AuthorNameAge author : authors) {
            System.out.println("Author name: " + author.getName()
                    + " | Age: " + author.getAge());
        }
    }
}
