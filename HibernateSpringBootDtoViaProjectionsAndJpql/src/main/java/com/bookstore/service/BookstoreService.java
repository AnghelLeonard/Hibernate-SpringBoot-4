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

    public List<AuthorNameAge> fetchByGenre1() {

        return authorRepository.fetchByGenre1("Anthology");
    }
    
    public List<AuthorNameAge> fetchByGenre2() {

        return authorRepository.fetchByGenre2("Anthology", 2);
    }
    
    public List<AuthorNameAge> fetchByGenre3() {

        return authorRepository.fetchByGenre3("Anthology", Limit.of(2));
    }
}
