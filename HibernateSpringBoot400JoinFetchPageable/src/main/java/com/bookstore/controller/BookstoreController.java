package com.bookstore.controller;

import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.service.BookstoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
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

    @GetMapping("/authorsAndBooksCQ")
    public ResponseEntity<Page<Author>> fetchWithBooksByGenreCQ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @SortDefaults({
                @SortDefault(sort = "name", direction = Sort.Direction.ASC),
                @SortDefault(sort = "id", direction = Sort.Direction.ASC) 
            }) Sort sort) {

        return ResponseEntity.ok(bookstoreService.fetchWithBooksByGenreCQ(page, size, sort));
    }
    
    @GetMapping("/authorsAndBooksEG")
    public ResponseEntity<Page<Author>> fetchWithBooksByGenreEG(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @SortDefaults({
                @SortDefault(sort = "name", direction = Sort.Direction.ASC),
                @SortDefault(sort = "id", direction = Sort.Direction.ASC) 
            }) Sort sort) {

        return ResponseEntity.ok(bookstoreService.fetchWithBooksByGenreEG(page, size, sort));
    }
    
    @GetMapping("/authorsAndBooksIsbnCQ")
    public ResponseEntity<Page<Book>> fetchWithAuthorsByIsbnCQ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @SortDefault(sort = "title", direction = Sort.Direction.ASC) Sort sort) {

        return ResponseEntity.ok(bookstoreService.fetchWithAuthorsByIsbnCQ(page, size, sort));
    }
    
    @GetMapping("/authorsAndBooksIsbnEG")
    public ResponseEntity<Page<Book>> fetchWithAuthorsByIsbnEG(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @SortDefault(sort = "title", direction = Sort.Direction.ASC) Sort sort) {

        return ResponseEntity.ok(bookstoreService.fetchWithAuthorsByIsbnEG(page, size, sort));
    }
}
