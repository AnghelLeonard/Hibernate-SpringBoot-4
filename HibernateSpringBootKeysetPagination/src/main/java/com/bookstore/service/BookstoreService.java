package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> fetchNextPage(long id, int limit) {
        return authorRepository.fetchAll(id, limit);
    }
    
    public List<Author> fetchNextPageable(long id, int limit) {        
        Pageable pageable = PageRequest.of(0, limit);
        
        return authorRepository.fetchAllPageable(id, pageable);
    }
    
    public List<AuthorDto> fetchNextPageDto(long id, int limit) {
        return authorRepository.fetchAllDto(id, limit);
    }
}
