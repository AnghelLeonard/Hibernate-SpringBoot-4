package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
import com.bookstore.service.BookstoreService;
import java.time.Instant;
import org.springframework.data.domain.Slice;
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

    @GetMapping("/authors")
    public ResponseEntity<Slice<AuthorDto>> fetchNextPageOfAuthors(
            @RequestParam(required = false) Instant lastTime,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageOfAuthors(lastTime, lastId, size));
    }      
}
