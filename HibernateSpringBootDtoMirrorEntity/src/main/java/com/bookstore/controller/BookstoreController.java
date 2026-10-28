package com.bookstore.controller;

import com.bookstore.dto.AuthorSummary;
import com.bookstore.dto.RecordAuthorSummary;
import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/authors/1")
    public List<Author> fetchViafindAll() {
        return bookstoreService.fetchViafindAll();
    }
    
    @GetMapping("/authors/2")
    public List<AuthorSummary> fetchViafindBy() {
        return bookstoreService.fetchViafindBy();
    }
    
    @GetMapping("/authors/3")
    public List<AuthorSummary> fetchAsDto() {
        return bookstoreService.fetchAsDto();
    }

    @GetMapping("/authors/4")
    public List<AuthorSummary> fetchAsDtoColumns() {
        return bookstoreService.fetchAsDtoColumns();
    }
    
    @GetMapping("/authors/5")
    public List<AuthorSummary> fetchAsDtoNative() {
        return bookstoreService.fetchAsDtoNative();
    }
    
    @GetMapping("/authors/6")
    public List<RecordAuthorSummary> fetchAsRecordDto() {
        return bookstoreService.fetchAsRecordDto();
    }
}
