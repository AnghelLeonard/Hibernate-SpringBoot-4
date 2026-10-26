package com.bookstore.service;

import com.bookstore.dto.AuthorSummary;
import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;
    }
    
    public List<AuthorSummary> findBy() {
        List<AuthorSummary> authors = authorRepository.findBy();

        return authors;
    }   
}
