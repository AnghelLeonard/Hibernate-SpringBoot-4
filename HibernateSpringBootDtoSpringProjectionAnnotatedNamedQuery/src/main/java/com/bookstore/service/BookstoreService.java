package com.bookstore.service;

import java.util.List;
import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import com.bookstore.dto.AuthorNameAge;
import org.springframework.data.domain.Limit;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorNameAge> fetchAuthorsNamesAndAges() {

        return authorRepository.fetchNameAndAge(Limit.of(2));
    }
    
    public List<String> fetchAuthorsNames() {

        return authorRepository.fetchName(Limit.of(2));
    }
}
