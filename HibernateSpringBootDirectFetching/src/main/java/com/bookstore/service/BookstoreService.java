package com.bookstore.service;

import com.bookstore.dao.Dao;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private static final Logger log = LoggerFactory.getLogger(BookstoreService.class);

    private final AuthorRepository authorRepository;
    private final Dao dao;

    public BookstoreService(AuthorRepository authorRepository, Dao dao) {
        this.authorRepository = authorRepository;
        this.dao = dao;
    }

    @Transactional(readOnly=true)
    public void directFetching() {
        // direct fetching via Spring Data
        Optional<Author> resultSD = authorRepository.findById(1L);
        log.info("Direct fetching via Spring Data result: {}", resultSD.orElse(null));

        // direct fetching via EntityManager
        Optional<Author> resultEM = dao.find(Author.class, 1L);
        log.info("Direct fetching via EntityManager result: {}", resultEM.orElse(null));

        // direct fetching via Session
        Optional<Author> resultHS = dao.findViaSession(Author.class, 1L);
        log.info("Direct fetching via Session result: {}", resultHS.orElse(null));
    }
}
