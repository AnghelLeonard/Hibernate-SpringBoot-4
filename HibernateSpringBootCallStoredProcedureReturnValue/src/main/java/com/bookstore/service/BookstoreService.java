package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void countAnthologyAuthors() {
        Integer result = authorRepository.countByGenre("Anthology");
        System.out.println("Result: " + result);
    }
    
    public void countAuthorsOlderThan50() {
        Integer result = authorRepository.countByGtAge(50);
        System.out.println("Result: " + result);
    }
}
