package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.view.AuthorView;
import com.bookstore.view.AuthorViewDto;
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

    public AuthorView fetchNextPage(long id, int limit) {
        List<Author> authors = authorRepository.fetchAll(id, limit + 1);

        if (authors.size() == (limit + 1)) {
            authors.remove(authors.size() - 1);
            
            return new AuthorView(authors, false);
        }

        return new AuthorView(authors, true);
    }
    
    public AuthorView fetchNextPageable(long id, int limit) {
        
        Pageable pageable = PageRequest.of(0, limit + 1);
        
        List<Author> authors = authorRepository.fetchAllPageable(id, pageable);
        
        if (authors.size() == (limit + 1)) {
            authors.remove(authors.size() - 1);
            
            return new AuthorView(authors, false);
        }

        return new AuthorView(authors, true);
    }

    public AuthorViewDto fetchNextPageDto(long id, int limit) {
        List<AuthorDto> authors = authorRepository.fetchAllDto(id, limit + 1);

        if (authors.size() == (limit + 1)) {
            authors.remove(authors.size() - 1);
            return new AuthorViewDto(authors, false);
        }

        return new AuthorViewDto(authors, true);
    }

    // Or, like this (rely on Author.toString() method):
    /*
    public Map<List<Author>, Boolean> fetchNextPage(long id, int limit) {
        List<Author> authors = authorRepository.fetchAll(id, limit + 1);

        if (authors.size() == (limit + 1)) {
            authors.remove(authors.size() - 1);
            return Collections.singletonMap(authors, false);
        }

        return Collections.singletonMap(authors, true);
    }
    */
}
