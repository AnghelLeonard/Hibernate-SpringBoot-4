package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private static final Logger log = Logger.getLogger(BookstoreService.class.getName());   
    
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    public BookstoreService(AuthorRepository authorRepository, AuthorService authorService) {
        this.authorRepository = authorRepository;
        this.authorService = authorService;
    }

    public void mainAuthor() {
        Author author = new Author();
        author.setId(6L);
        // persistAuthor(author);
        authorService.persistAuthor(author);
        notifyAuthor(author);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private long persistAuthor(Author author) {
        authorRepository.save(author);
        return authorRepository.count();
    }

    private void notifyAuthor(Author author) {
        log.info(() -> "Saving author: " + author);
    }
}
