package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import com.bookstore.projection.AuthorNameBookTitlePrice;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository; 

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;        
    }
 
    public List<AuthorNameBookTitlePrice> fetchAuthorsOfTop3PriceBooks() {
        return authorRepository.findAuthorsOfTop3PriceBooks();
    }   
}
