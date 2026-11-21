package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.BookDto;
import com.bookstore.service.BookstoreService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    // GET /api/authors?filter=(age:gt:40 AND genre:eq:Anthology) OR (books.price:lt:50)
    @GetMapping("/authors")
    public List<AuthorDto> getAuthors(@RequestParam(required = false) String filter) {
        return bookstoreService.searchAuthors(filter);
    }

    // GET /api/books?filter=price:between:20,60 AND author.genre:in:Anthology,History&page=0&size=5&sort=title,asc
    @GetMapping("/books")
    public Page<BookDto> getBooks(@RequestParam(required = false) String filter, Pageable pageable) {
        return bookstoreService.searchBooks(filter, pageable);
    }
}
