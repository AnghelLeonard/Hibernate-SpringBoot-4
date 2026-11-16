package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.repository.AuthorRepository;
import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> fetchNextPage(int size, int page) {

        return authorRepository.fetchAll(size, page);
    }
    
    public List<AuthorDto> fetchLimitSize(int size) {

        return authorRepository.fetchAllLimit(Limit.of(size));
    }
}
