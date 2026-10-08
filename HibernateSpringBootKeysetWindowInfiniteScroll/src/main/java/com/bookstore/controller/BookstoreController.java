package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.KeysetDto;
import com.bookstore.service.BookstoreService;
import java.time.Instant;
import org.springframework.format.annotation.DateTimeFormat;
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
     public ResponseEntity<KeysetDto<AuthorDto>> fetchNextPageOfAuthors(            
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastCreatedAt,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {

        KeysetDto<AuthorDto> response = bookstoreService.fetchNextPageOfAuthors(lastCreatedAt, lastId, size);
        
        return ResponseEntity.ok(response);
    }    
}
