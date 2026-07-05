package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void fetchAuthorByGenre() {
        List<AuthorDto> result = authorRepository.fetchAuthorByGenre("Anthology");
        
        System.out.println("Result: " + result);
        
      //  result.get(0).setGenre("cccc");
    }
    
    @Transactional
    public void fetchAuthorIdNameByGenre() {
        Object[] result = authorRepository.fetchAuthorIdNameByGenre("Anthology");
        
        System.out.println("Result: " + result);                
    }
}
