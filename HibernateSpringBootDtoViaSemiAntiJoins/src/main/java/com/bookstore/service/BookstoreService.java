package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import com.bookstore.projection.AuthorName;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository; 

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;        
    }
 
    public List<AuthorName> fetchAuthorsHavingBooks() {
        return authorRepository.findAuthorsHavingBooks();
    }   
    
    public List<AuthorName> fetchAuthorsNoBooks() {
        return authorRepository.findAuthorsNoBooks();
    }
}
