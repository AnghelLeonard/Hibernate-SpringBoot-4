package com.bookstore.controller;

import com.bookstore.service.BookstoreService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.dto.BookSummary;
import com.bookstore.dto.RecordBookSummary;
import com.bookstore.dto.RecordBookView;
import com.bookstore.dto.SimpleBookSummary;
import com.bookstore.dto.VirtualBookSummary;
import com.bookstore.dto.view.Views;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/booksAndauthors/1")
    public List<BookSummary> fetchBooksWithAuthorsQueryBuilderMechanism() {
        return bookstoreService.fetchBooksWithAuthorsQueryBuilderMechanism();
    }

    @GetMapping("/booksAndauthors/2")
    public List<BookSummary> fetchBooksWithAuthorsViaQuery() {
        return bookstoreService.fetchBooksWithAuthorsViaQuery();
    }

    @GetMapping("/booksAndauthors/3")
    public List<SimpleBookSummary> fetchBooksWithAuthorsViaQuerySimpleDto() {
        return bookstoreService.fetchBooksWithAuthorsViaQuerySimpleDto();
    }    
    
    @GetMapping("/booksAndauthors/4")
    public List<Object[]> fetchBooksWithAuthorsViaArrayOfObjects() {
        return bookstoreService.fetchBooksWithAuthorsViaArrayOfObjects();
    }
    
    @GetMapping("/booksAndauthors/5")
    public List<VirtualBookSummary> fetchBooksWithAuthorsViaQueryVirtualDto() {
        return bookstoreService.fetchBooksWithAuthorsViaQueryVirtualDto();
    }
    
    @GetMapping("/booksAndauthors/6")
    public List<RecordBookSummary> fetchBooksWithAuthorsViaQueryRecordDto() {
        return bookstoreService.fetchBooksWithAuthorsViaQueryRecordDto();
    }
    
    @GetMapping("/booksAndauthors/summary")
    @JsonView(Views.BookSummary.class)
    public List<RecordBookView> fetchByViaQuerySimpleRecordViewSummary() {
        return bookstoreService.fetchByViaQuerySimpleRecordView();
    }
    
    @GetMapping("/booksAndauthors/detailed")
    @JsonView(Views.BookDetailed.class)
    public List<RecordBookView> fetchByViaQuerySimpleRecordViewDetailed() {
        return bookstoreService.fetchByViaQuerySimpleRecordView();
    }
}
