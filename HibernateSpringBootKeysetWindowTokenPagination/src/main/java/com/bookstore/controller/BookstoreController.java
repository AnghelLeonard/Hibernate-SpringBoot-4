package com.bookstore.controller;

import com.bookstore.dto.WindowResponse;
import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/authors")
     public ResponseEntity<WindowResponse<Author>> fetchNextPageOfAuthors(            
            @RequestParam(required = false) String resumeToken,
            @RequestParam(defaultValue = "10") int size) {

        WindowResponse<Author> response = bookstoreService.fetchNextPageOfAuthors(resumeToken, size);
        
        return ResponseEntity.ok(response);
    }    
}
