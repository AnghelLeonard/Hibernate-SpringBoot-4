package com.bookstore.service;

import java.util.List;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.AuthorRepository.AuthorNameAge;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorNameAge> findFirst2ByGenreOrderByName() {

        return authorRepository.findFirst2ByGenreOrderByName("Anthology");
    }
    
    public List<AuthorNameAge> findByGenreOrderByName() {

        return authorRepository.findByGenreOrderByName("Anthology", Limit.of(2));
    }
}
