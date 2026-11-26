package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;   

    public BookstoreService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;        
    }

    public List<Author> fetchAuthors() {
        
        // with optional = false on Author.book, this executes ONLY 1 query:
        // select a1_0.id,a1_0.age,a1_0.genre,a1_0.name from author a1_0

        List<Author> authors = authorRepository.findAll();

        // Book is still an uninitialized lazy proxy here (no queries to "book" yet)
        return authors;
    }
}
