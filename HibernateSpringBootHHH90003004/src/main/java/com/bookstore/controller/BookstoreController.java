package com.bookstore.controller;

import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/authorsAndBooksPage")
    public Page<Author> fetchPageOfAuthorsWithBooksByGenre( 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return bookstoreService.fetchPageOfAuthorsWithBooksByGenre(page, size);
    }

    @GetMapping("/authorsAndBooksSlice")
    public Slice<Author> fetchSliceOfAuthorsWithBooksByGenre( 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return bookstoreService.fetchSliceOfAuthorsWithBooksByGenre(page, size);
    }

    @GetMapping("/authorsAndBooksList")
    public List<Author> fetchListOfAuthorsWithBooksByGenre(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return bookstoreService.fetchListOfAuthorsWithBooksByGenre(page, size);
    }
    
    @GetMapping("/authorsAndBooksPageEG")
    public Page<Author> fetchPageOfAuthorsWithBooksByGenreEntityGraph(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return bookstoreService.fetchPageOfAuthorsWithBooksByGenreEntityGraph(page, size);
    }
    
    @GetMapping("/authorsAndBooksPageTuple")
    public Page<Author> fetchPageOfAuthorsWithBooksByGenreTuple(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return bookstoreService.fetchPageOfAuthorsWithBooksByGenreTuple(page, size);
    }
}
