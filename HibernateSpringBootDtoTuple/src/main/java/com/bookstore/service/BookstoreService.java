package com.bookstore.service;

import java.util.List;
import com.bookstore.repository.AuthorRepository;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Tuple> fetchAuthorsJpql() {

        return authorRepository.fetchAuthorsJpql();
    }
    
    public List<Tuple> fetchAuthorsNative() {

        return authorRepository.fetchAuthorsNative();
    }
}
