package com.bookstore.controller;

import com.bookstore.record.dto.RecordAuthorSummary;
import com.bookstore.service.BookstoreService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.spring.dto.AuthorSummary;
import com.bookstore.spring.dto.SimpleAuthorSummary;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/authorsAndbooks/1")
    public List<AuthorSummary> fetchAuthorsWithBooksQueryBuilderMechanism() {
        return bookstoreService.fetchAuthorsWithBooksQueryBuilderMechanism();
    }

    @GetMapping("/authorsAndbooks/2")
    public List<AuthorSummary> fetchAuthorsWithBooksViaQuery() {
        return bookstoreService.fetchAuthorsWithBooksViaQuery();
    }
    
    @GetMapping("/authorsAndbooks/3")
    public List<AuthorSummary> fetchAuthorsWithBooksViaJoinFetch() {
        return bookstoreService.fetchAuthorsWithBooksViaJoinFetch();
    }

    @GetMapping("/authorsAndbooks/4")
    public List<SimpleAuthorSummary> fetchAuthorsWithBooksViaQuerySimpleDto() {
        return bookstoreService.fetchAuthorsWithBooksViaQuerySimpleDto();
    }
    
    @GetMapping("/authorsAndbooks/5")
    public List<RecordAuthorSummary> fetchAuthorsWithBooksViaQueryRecordDto() {
        return bookstoreService.fetchAuthorsWithBooksViaQueryRecordDto();
    }
    
    @GetMapping("/authorsAndbooks/6")
    public List<Object[]> fetchAuthorsWithBooksViaArrayOfObjects() {
        return bookstoreService.fetchAuthorsWithBooksViaArrayOfObjects();
    }       
    
    @GetMapping("/authorsAndbooks/7")
    public List<com.bookstore.transform.dto.AuthorSummary> fetchAuthorsWithBooksViaArrayOfObjectsAndTransformToDto() {
        return bookstoreService.fetchAuthorsWithBooksViaArrayOfObjectsAndTransformToDto();
    }
    
    @GetMapping("/authorsAndbooks/8")
    public List<com.bookstore.jdbc.template.dto.AuthorSummary> fetchAuthorsWithBooksViaJdbcTemplateToDto() {
        return bookstoreService.fetchAuthorsWithBooksViaJdbcTemplateToDto();
    }
}
