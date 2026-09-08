package com.bookstore.controller;

import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookstoreController {
    
    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/first10")
    public List<Author> fetchAuthorsBooksPublishersReviewersReviews() {

        long start = System.nanoTime();

        List<Author> result = bookstoreService.fetchAuthorsBooksTagsPublishersReviewersReviews();
        
        long end = System.nanoTime();
        
        long durationInMs = TimeUnit.NANOSECONDS.toMillis((end-start));
            
        System.out.println("Query time: " + durationInMs + " ms");

        return result.subList(0, 10); // Don't do this in production! 
                                      // Don't extract a result set and then truncate it on return.
    }
}
