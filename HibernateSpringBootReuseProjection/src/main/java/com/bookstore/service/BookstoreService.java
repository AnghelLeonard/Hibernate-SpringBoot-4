package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.dto.AuthorSummary;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;
    }
    
    public List<AuthorSummary> findAllBy() {
        List<AuthorSummary> authors = authorRepository.findBy();

        return authors;
    }    
    
    public List<AuthorSummary> fetchAll() {
        List<AuthorSummary> authors = authorRepository.fetchAll();

        return authors;
    }
        
    public List<AuthorSummary> fetchAgeNameGenre() {
        List<AuthorSummary> authors = authorRepository.fetchAgeNameGenre();

        return authors;
    }
    
    public List<AuthorSummary> fetchNameEmail() {
        List<AuthorSummary> authors = authorRepository.fetchNameEmail();

        return authors;
    }

}
