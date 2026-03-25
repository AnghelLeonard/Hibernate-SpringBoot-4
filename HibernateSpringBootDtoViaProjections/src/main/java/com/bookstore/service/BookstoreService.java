package com.bookstore.service;

import java.util.List;
import com.bookstore.projection.AuthorNameAge;
import com.bookstore.repository.AuthorRepository;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorNameAge> fetchFirst2ByGenre() {

        return authorRepository.findFirst2ByGenre("Anthology");
    }
    
    public List<AuthorNameAge> fetchByGenre() {

        return authorRepository.findByGenre("Anthology", Limit.of(2));
    }
}
