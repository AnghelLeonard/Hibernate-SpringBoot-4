package com.bookstore.controller;

import com.bookstore.dto.WindowDto;
import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/authors")
    public ResponseEntity<WindowDto<Author>> getProducts(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String token,
            @RequestParam(defaultValue = "forward") String direction) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageOfAuthors(size, token, direction));
    }
}
