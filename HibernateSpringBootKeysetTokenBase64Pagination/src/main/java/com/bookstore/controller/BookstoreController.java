package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.KeysetDto;
import com.bookstore.service.BookstoreService;
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
    public KeysetDto<AuthorDto> fetchNextPageOfAuthors(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return bookstoreService.fetchNextPageOfAuthors(cursor, size);
    }
    
   
}
