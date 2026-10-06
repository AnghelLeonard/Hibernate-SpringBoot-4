package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.KeysetDto;
import com.bookstore.service.BookstoreService;
import java.time.Instant;
import java.util.List;
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
    public ResponseEntity<List<AuthorDto>> fetchNextPageOfAuthors(
            @RequestParam(required = false) Instant lastTime,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageOfAuthors(lastTime, lastId, size));
    }
    
    @GetMapping("/authors/metadata")
    public ResponseEntity<KeysetDto<AuthorDto>> fetchNextPageOfAuthorsWithMetadata(
            @RequestParam(required = false) Instant lastTime,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageOfAuthorsWithMetadata(lastTime, lastId, size));
    }
}
