package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class BookstoreService {

    private static final Logger log = LoggerFactory.getLogger(BookstoreService.class);

    private final AuthorRepository authorRepository;
    private final TransactionTemplate template;

    public BookstoreService(AuthorRepository authorRepository, TransactionTemplate template) {
        this.authorRepository = authorRepository;
        this.template = template;
    }

    public void process() {

        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        // PostgreSQL has the default Isolation level: READ_COMMITTED
        // But, if you will swtich to MySQL then the default will be REPEATABLE_READ        
        template.setIsolationLevel(Isolation.READ_COMMITTED.value());

        // Transaction A
        template.executeWithoutResult(statusA -> {

            Author authorA1 = authorRepository.findById(1L).orElseThrow();
            log.info("Author A1: {}\n", authorA1.getName());

            // Transaction B
            template.executeWithoutResult(statusB -> {

                Author authorB = authorRepository.findById(1L).orElseThrow();
                authorB.setName("Alicia Tom");
                log.info("Author B: {}", authorB.getName());
            });

            // Direct fetching via findById() or find() doesn't trigger a SELECT
            // It loads the author directly from Persistence Context
            Author authorA2 = authorRepository.findById(1L).orElseThrow();
            log.info("\nAuthor A2: {}\n", authorA2.getName());

            // JPQL entity queries take advantage of session-level repeatable reads
            // The data snapshot returned by the triggered SELECT is ignored
            Author authorViaJpql = authorRepository.fetchByIdJpql(1L);
            log.info("Author via JPQL: {}", authorViaJpql.getName());

            // SQL entity queries take advantage of session-level repeatable reads
            // The data snapshot returned by the triggered SELECT is ignored
            Author authorViaSql = authorRepository.fetchByIdSql(1L);
            log.info("Author via SQL: {}", authorViaSql.getName());

            // JPQL query projections always load the latest database state
            String nameViaJpql = authorRepository.fetchNameByIdJpql(1L);
            log.info("Author name via JPQL: {}", nameViaJpql);

            // SQL query projections always load the latest database state
            String nameViaSql = authorRepository.fetchNameByIdSql(1L);
            log.info("Author name via SQL: {}", nameViaSql);
        });
    }
}
