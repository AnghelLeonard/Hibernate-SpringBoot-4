package com.bookstore.controller;

import com.bookstore.service.BookstoreService;
import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/byGenre")
    public List<AuthorRepository.AuthorNameAge> fetchByGenre() {
        return bookstoreService.findByGenre();
    }
    
    @GetMapping("/first2ByGenre")
    public List<AuthorRepository.AuthorNameAge> fetchFirst2ByGenre() {
        return bookstoreService.findFirst2ByGenre();
    }

}
