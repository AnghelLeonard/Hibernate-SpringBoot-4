package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> fetchNextPage(int page, int size) {

        return authorRepository.fetchAll(page, size);
    }
    
    public List<Author> fetchNextPageJpql(int page, int size) {
        
        int safePage = Math.max(1, page); 
        int offset = (safePage - 1) * size;

        return authorRepository.fetchAllJpql(size, offset);
    }

     public List<Author> fetchNextPageJpqlAnsi(int page, int size) {
        
        int safePage = Math.max(1, page); 
        int offset = (safePage - 1) * size;

        return authorRepository.fetchAllJpqlAnsi(offset, size);
    }
}
