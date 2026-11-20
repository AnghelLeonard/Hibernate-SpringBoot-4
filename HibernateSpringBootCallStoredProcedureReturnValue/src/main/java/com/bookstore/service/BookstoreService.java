package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void countAnthologyAuthors1() {
        Integer result = authorRepository.countByGenre("Anthology");
        System.out.println("Result: " + result);
    }
    
    public void countAnthologyAuthors2() {
        Integer result = authorRepository.countByGenre("Anthology");
        System.out.println("Result: " + result);
    }
    
    public void countAnthologyAuthors3() {
        Integer result = authorRepository.countByGenreFunc("Anthology");
        System.out.println("Result: " + result);
    }
}
