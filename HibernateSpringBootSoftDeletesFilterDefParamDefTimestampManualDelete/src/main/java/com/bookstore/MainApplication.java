package com.bookstore;

import com.bookstore.entity.Author;
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
          /*  
            System.out.println("\n\nShow active authors (initially, all authors are active)");            
            System.out.println(bookstoreService.getActiveAuthors());
            
            System.out.println("\n\nShow active books (initially, all books are active)");
            System.out.println(bookstoreService.getActiveBooks());            
            
            System.out.println("\n\nSoft-delete the author with ID 1 and show the active authors and books");
            bookstoreService.softDeleteAuthor(1L);
            System.out.println(bookstoreService.getActiveAuthors());
            System.out.println(bookstoreService.getActiveBooks());
            
            */System.out.println("\n\nSoft-delete the book with ID 2 (the author remains untached)");
            bookstoreService.softDeleteBook(2L);/*
            System.out.println(bookstoreService.getActiveAuthors());
            System.out.println(bookstoreService.getActiveBooks());
            
             System.out.println("\n\nFor admins show all authors and books (include soft deleted)");
             System.out.println(bookstoreService.getAllAuthorsIncludingDeleted());
            System.out.println(bookstoreService.getAllBooksIncludingDeleted());*/
        };
    }
}
