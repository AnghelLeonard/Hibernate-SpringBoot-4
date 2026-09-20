package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void save3Authors() throws InterruptedException {

        for (int i = 1; i <= 10; i++) {
            
            Thread.sleep(1000);
            
            Author author = new Author();
            author.setName("Author_" + i);

            authorRepository.save(author); 
        }
    }
}
