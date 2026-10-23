package com.bookstore.service;

import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookstoreClerk;
import com.bookstore.repository.BookstoreClerkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookstoreClerkRepository bookstoreClerkRepository;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository, BookstoreClerkRepository bookstoreClerkRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookstoreClerkRepository = bookstoreClerkRepository;
    }

    @Transactional
    public void addBookToAuthor() {
        // behind getReferenceById() we have EntityManager#getReference()
        Author proxy = authorRepository.getReferenceById(1L);

        Book book = new Book();
        book.setIsbn("001-MJ");
        book.setTitle("The Canterbury Anthology");
        book.setAuthor(proxy);

        bookRepository.save(book);
    }
    
    // leads to a SELECT and DELETE    
    public void removeDetachedAuthor() {
        Book proxy = bookRepository.getReferenceById(1L);
        bookRepository.delete(proxy);
    }
    
    // leads to a SELECT and DELETE   
    public void removeDetachedBookstoreClerk() {
        BookstoreClerk proxy = bookstoreClerkRepository.getReferenceById(1L);
        bookstoreClerkRepository.delete(proxy);
    }
}
