package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.Views;
import com.bookstore.service.BookstoreService;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/1")
    @JsonView(Views.NameEmail.class)
    public List<AuthorDto> findNameEmailBy() {
        return bookstoreService.findBy();
    }

    @GetMapping("/2")
    @JsonView(Views.NameEmailAgeGenre.class)
    public List<AuthorDto> findNameEmailAgeGenreBy() {
        return bookstoreService.findBy();
    }
    
    @GetMapping("/3")
    @JsonView(Views.All.class)
    public List<AuthorDto> findAllBy() {
        return bookstoreService.findBy();
    }
}
