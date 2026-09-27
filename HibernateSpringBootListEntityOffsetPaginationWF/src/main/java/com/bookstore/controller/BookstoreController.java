package com.bookstore.controller;

import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/authors/{page}/{size}")
    public List<Author> fetchAuthors(@PathVariable int page, @PathVariable int size) {

        return bookstoreService.fetchNextPage(page, size);
    }

    @GetMapping("/authors/jpql/{page}/{size}")
    public List<Author> fetchAuthorsJpql(@PathVariable int page, @PathVariable int size) {

        return bookstoreService.fetchNextPageJpql(page, size);
    }
    
    @GetMapping("/authors/ansi/{page}/{size}")
    public List<Author> fetchAuthorsJpqlAnsi(@PathVariable int page, @PathVariable int size) {

        return bookstoreService.fetchNextPageJpqlAnsi(page, size);
    }
}
