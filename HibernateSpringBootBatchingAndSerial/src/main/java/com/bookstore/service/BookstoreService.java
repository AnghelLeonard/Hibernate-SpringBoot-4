package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void authorBatchInserts() {

        List authors = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {

            Author author = new Author();

            author.setAge(0);
            author.setGenre("Genre_" + i);
            author.setName("Name_" + i);

            authors.add(author);
        }

        authorRepository.saveAll(authors);
    }
}
