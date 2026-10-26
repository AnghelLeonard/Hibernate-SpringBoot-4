package com.bookstore.controller;

import com.bookstore.service.BookstoreService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.dto.AuthorSummary;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }
    
    @GetMapping("/1")
    public List<AuthorSummary> findAllBy() {
        return bookstoreService.findAllBy();
    }

    @GetMapping("/2")
    public List<AuthorSummary> fetchAll() {
        return bookstoreService.fetchAll();
    }
    
    @GetMapping("/3")
    public List<AuthorSummary> fetchAgeNameGenre() {
        
        return bookstoreService.fetchAgeNameGenre();               
    }

    @GetMapping("/4")
    public List<AuthorSummary> fetchNameEmail() {
        return bookstoreService.fetchNameEmail();
    }
}
