package com.bookstore.controller;

import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/authors/jpql")
    public ResponseEntity<Page<Author>> fetchAuthorsJpql(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageJpql(page, size));
    }

    @GetMapping("/authors/native")
    public ResponseEntity<Page<Author>> fetchAuthorsNative(            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageNative(page, size));
    }
}
