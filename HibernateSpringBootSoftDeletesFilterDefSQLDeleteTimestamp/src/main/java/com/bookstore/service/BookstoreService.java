package com.bookstore.service;

import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @Transactional
    public void softDeleteAuthor() {
        Author author = authorRepository.findById(1L).get();

        authorRepository.delete(author);
    }

    @Transactional(readOnly = true)
    public void displayAllIncludeDeletedAuthors() {

        entityManager.unwrap(Session.class)
                .disableFilter("excludeDeleted");

        List<Author> authors = authorRepository.findAll();

        System.out.println("\nAll authors including deleted:");
        authors.forEach(a -> System.out.println("Author name: " + a.getName() + " deleted " + a.getDeleted()));
        System.out.println();
    }

    public void displayAllExceptDeletedAuthors() {

        List<Author> authors = authorRepository.findAll();

        System.out.println("\nAll authors except deleted:");
        authors.forEach(a -> System.out.println("Author name: " + a.getName() + " deleted " + a.getDeleted()));
        System.out.println();
    }

    public void displayAllOnlyDeletedAuthors() {
        List<Author> authors = authorRepository.findAllOnlyDeleted();

        System.out.println("\nAll deleted authors:");
        authors.forEach(a -> System.out.println("Author name: " + a.getName() + " deleted " + a.getDeleted()));
        System.out.println();
    }

    @Transactional
    public void softDeleteBook() {
        Author author = authorRepository.findById(4L).get();
        Book book = author.getBooks().get(0);

        author.removeBook(book);
    }
    
    @Transactional(readOnly = true)
    public void displayAllIncludeDeletedBooks() {
        
        entityManager.unwrap(Session.class)
                .disableFilter("excludeDeleted");
        
        List<Book> books = bookRepository.findAll();

        System.out.println("\nAll books including deleted:");
        books.forEach(b -> System.out.println("Book title: " + b.getTitle() + " deleted " + b.getDeleted()));
        System.out.println();
    }
    
     public void displayAllExceptDeletedBooks() {
        List<Book> books = bookRepository.findAll();

        System.out.println("\nAll books except deleted:");
        books.forEach(b -> System.out.println("Book title: " + b.getTitle() + " deleted " + b.getDeleted()));
        System.out.println();
    }
    
     public void displayAllOnlyDeletedBooks() {
        List<Book> books = bookRepository.findAllOnlyDeleted();

        System.out.println("\nAll deleted books:");
        books.forEach(b -> System.out.println("Book title: " + b.getTitle() + " deleted " + b.getDeleted()));
        System.out.println();
    }

    @Transactional
    public void restoreBook() {
        bookRepository.restoreById(1L);
    }

    @Transactional
    public void restoreAuthor() {
        authorRepository.restoreById(1L);
        bookRepository.restoreByAuthorId(1L);
    }       
}
