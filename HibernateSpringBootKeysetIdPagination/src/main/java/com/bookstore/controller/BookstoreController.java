package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
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
    public ResponseEntity<List<Author>> fetchNextPageOfAuthors(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageOfAuthors(lastId, size));
    }
    
    @GetMapping("/authors/pageable")
    public ResponseEntity<List<Author>> fetchNextPageableOfAuthors(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageableOfAuthors(lastId, size));
    }

    @GetMapping("/authors/dto")
    public ResponseEntity<List<AuthorDto>> fetchNextPageAsDtoOfAuthors(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageAsDtoOfAuthors(lastId, size));
    }
}
