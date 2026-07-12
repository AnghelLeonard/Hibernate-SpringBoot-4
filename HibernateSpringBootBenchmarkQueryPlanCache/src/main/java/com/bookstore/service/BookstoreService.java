package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;    

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;        
    }

    @Transactional(readOnly = true)
    public void fetch() {
        authorRepository.fetchAuthorsBooksByPriceInnerJoin((int) (Math.random() * 100));
        authorRepository.fetchByAge((int) (Math.random() * 100));
        authorRepository.fetchAuthorsBooksByPriceJoinFetch((int) (Math.random() * 100));
    }
}
