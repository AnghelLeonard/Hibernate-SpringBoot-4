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

    public List<Author> fetchNextPageOfAuthors(Long lid, int size) {
        return authorRepository.fetchNextPage(lid, size);
    }
    
    public List<Author> fetchNextPageableOfAuthors(Long lid, int size) {        
        Pageable pageable = PageRequest.of(0, size);
        
        return authorRepository.fetchNextPageable(lid, pageable);
    }
    
    public List<AuthorDto> fetchNextPageAsDtoOfAuthors(Long lid, int size) {
        return authorRepository.fetchNextPageDto(lid, size);
    }
}
