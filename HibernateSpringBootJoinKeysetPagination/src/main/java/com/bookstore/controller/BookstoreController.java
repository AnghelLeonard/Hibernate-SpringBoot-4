package com.bookstore.controller;

import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.KeysetDto;
import com.bookstore.service.BookstoreService;
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

    @GetMapping("/authorsAndBooksFlat")
    public ResponseEntity<KeysetDto<AuthorBookFlatDto>> fetchPageOfAuthorsWithBooksByGenreFlatDto(
            @RequestParam(required = false) Long lastAuthorId,
            @RequestParam(required = false) Long lastBookId,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService
                .fetchPageOfAuthorsWithBooksByGenreFlatDto(lastAuthorId, lastBookId, size));
    } 
    
    @GetMapping("/authorsAndBooks")
    public ResponseEntity<KeysetDto<AuthorDto>> fetchPageOfAuthorsWithBooksByGenreDto(
            @RequestParam(required = false) Long lastAuthorId,
            @RequestParam(required = false) Long lastBookId,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService
                .fetchPageOfAuthorsWithBooksByGenreDto(lastAuthorId, lastBookId, size));
    } 
}
