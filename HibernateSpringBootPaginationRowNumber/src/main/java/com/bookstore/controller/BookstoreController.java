package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
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
    public ResponseEntity<List<AuthorDto>> fetchAuthors(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int end
    ) {

        return ResponseEntity.ok(bookstoreService.fetchPageJpql(start, end));
    }
}
