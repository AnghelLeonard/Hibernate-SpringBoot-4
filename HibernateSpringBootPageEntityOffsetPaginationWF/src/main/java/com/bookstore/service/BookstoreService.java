package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }       
    
    public Page<Author> fetchNextPageJpql(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "age"));

        List<Author> authors = authorRepository.fetchAllJpql(pageable);
        Page<Author> pageOfAuthors = new PageImpl(authors, pageable,
                authors.isEmpty() ? 0 : authors.get(0).getTotal());

        return pageOfAuthors;
    }
    
    public Page<Author> fetchNextPageNative(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "age"));

        List<Author> authors = authorRepository.fetchAllNative(pageable);
        Page<Author> pageOfAuthors = new PageImpl(authors, pageable,
                authors.isEmpty() ? 0 : authors.get(0).getTotal());

        return pageOfAuthors;
    } 
}
