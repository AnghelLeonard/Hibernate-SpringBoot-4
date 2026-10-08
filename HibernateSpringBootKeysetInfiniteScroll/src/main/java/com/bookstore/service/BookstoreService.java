package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.repository.AuthorRepository;
import java.time.Instant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }         
    
    public Slice<AuthorDto> fetchNextPageOfAuthors(Instant ltime, Long lid, int size) {        
        Pageable pageable = PageRequest.of(0, size);
        
        return authorRepository.fetchNextPage(ltime, lid, pageable);
    }        
}
