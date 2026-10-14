package com.bookstore.service;

import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository, EntityManager entityManager) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
    }

    // returns only active authors     
    public List<Author> getActiveAuthors() {

        return authorRepository.findAll();
    }

    // Returns everything (both active and soft-deleted authors, e.g., for admin panels)    
    @Transactional(readOnly = true)
    public List<Author> getAllAuthorsIncludingDeleted() {

        entityManager.unwrap(Session.class)
                .disableFilter("activeOnlyFilter");

        return authorRepository.findAll();
    }

    @Transactional
    public void softDeleteAuthor(Long authorId) {
        // findById() bypasses Hibernate filters natively, so we manually check if it's already deleted
        // you can use findOne() or bookRepository.findAllById(List.of(bookId)).getFirst(); 
        // if you just want to see if a record is deleted then use, authorRepository.existsById(1L); 
        // to generate "select count(*) from author a1_0 where a1_0.deleted IS NULL and a1_0.id=?"      
        authorRepository.findById(authorId).ifPresent(author -> {
            if (!author.isDeleted()) {
                Instant deletionTime = Instant.now();

                // soft delete the parent
                author.setDeleted(deletionTime);

                // Cascade identical UTC timestamp to all child books
                author.getBooks().forEach(book -> book.setDeleted(deletionTime));
            }
        });
    }

    // returns only active books     
    public List<Book> getActiveBooks() {

        return bookRepository.findAll();
    }

    // Returns everything (both active and soft-deleted books, e.g., for admin panels) 
    @Transactional(readOnly = true)
    public List<Book> getAllBooksIncludingDeleted() {

        entityManager.unwrap(Session.class)
                .disableFilter("activeOnlyFilter");
                
        return bookRepository.findAll();
    }

    // soft deletes an individual book and cleans the parent's active collection in memory
    @Transactional
    public void softDeleteBook(Long bookId) {
        // findById() bypasses Hibernate filters natively, so we manually check if it's already deleted
        bookRepository.findById(bookId).ifPresent(book -> {
            if (!book.isDeleted()) {
                // set the current precise UTC timestamp
                book.setDeleted(Instant.now());

                // remove it from the list so author.getBooks() is accurate in-memory, 
                // but keeps the author_id intact in the database
                if (book.getAuthor() != null) {
                    book.getAuthor().removeSoftDeletedBookFromMemory(book);
                }
            }
        });
    }
}
