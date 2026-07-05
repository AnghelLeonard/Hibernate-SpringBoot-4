package com.bookstore.service;

import com.bookstore.dto.AuthorDtoC;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.dto.AuthorDtoI;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void fetchAuthorByGenre() {
        List<Author> result = authorRepository.fetchAuthorByGenre("Anthology");
        
        System.out.println("Result: " + result);              
    }
    
    @Transactional
    public void fetchNicknameAndAgeByGenre() {
        List<AuthorDtoI> result = authorRepository.fetchNicknameAndAgeByGenre("Anthology");
        
        System.out.println("Result (DTO): " + result);                
    }
    
    @Transactional
    public void fetchAuthorByGenreNSPQ() {
        List<Author> result = authorRepository.fetchAuthorByGenreNSPQ("Anthology");
        
        System.out.println("Result: " + result);              
    }
    
    @Transactional
    public void fetchNicknameAndAgeByGenreNSPQ() {
        List<AuthorDtoC> result = authorRepository.fetchNicknameAndAgeByGenreNSPQ("Anthology");
        
        System.out.println("Result (DTO): " + result);                
    }
}
