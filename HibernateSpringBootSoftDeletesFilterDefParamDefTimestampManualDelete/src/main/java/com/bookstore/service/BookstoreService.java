package com.bookstore.service;

import com.bookstore.resolver.SoftDeleteContext;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {    

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;        
    }

    // returns only active authors because includeDeleted defaults to false
    public List<Author> getActiveAuthors() {

        return authorRepository.findAll();
    }

    // Returns everything (both active and soft-deleted authors, e.g., for admin panels)
    public List<Author> getAllAuthorsIncludingDeleted() {
        try {
            SoftDeleteContext.setShowDeleted(true); // switches parameter extraction state globally
            return authorRepository.findAll();
        } finally {
            SoftDeleteContext.clear(); // safely resets back to default isolation state
        }
    }

    @Transactional
    public void softDeleteAuthor(Long authorId) {

        authorRepository.findById(authorId).ifPresent(author -> {
            if (!author.isDeleted()) {
                Instant deletionTime = Instant.now();

                // soft delete the parent
                author.setDeletedAt(deletionTime);

                // Cascade identical UTC timestamp to all child books
                author.getBooks().forEach(book -> book.setDeletedAt(deletionTime));
            }
        });
    }
    
    // returns only active books because includeDeleted defaults to false
    public List<Book> getActiveBooks() {

        return bookRepository.findAll();
    }

    // Returns everything (both active and soft-deleted books, e.g., for admin panels)    
    public List<Book> getAllBooksIncludingDeleted() {
        try {
            SoftDeleteContext.setShowDeleted(true); // switches parameter extraction state globally
            return bookRepository.findAll();
        } finally {
            SoftDeleteContext.clear(); // safely resets back to default isolation state
        }
    }

    // soft deletes an individual book and cleans the parent's active collection in memory
    @Transactional
    public void softDeleteBook(Long bookId) {
        // findById() bypasses Hibernate filters natively, so we manually check if it's already deleted
        // you can use findOne() or bookRepository.findAllById(List.of(bookId)).getFirst(); 
        // if you just want to see if a record is deleted then use, bookRepository.existsById(1L);
        bookRepository.findById(bookId).ifPresent(book -> {
            if (!book.isDeleted()) {
                // set the current precise UTC timestamp
                book.setDeletedAt(Instant.now());

                // remove it from the list so author.getBooks() is accurate in-memory, 
                // but keeps the author_id intact in the database
                if (book.getAuthor() != null) {
                    book.getAuthor().removeSoftDeletedBookFromMemory(book);
                }
            }
        });
    }
}
