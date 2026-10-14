package com.bookstore.service;

import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.resolver.SoftDeleteContext;
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

    @Transactional
    public void softDeleteAuthor() {
        Author author = authorRepository.findById(1L).get(); // bypass the filter
        // Author author = authorRepository.findAllById(List.of(1L)).getFirst(); // use the filter

        authorRepository.delete(author);
    }

    // returns everything (both active and soft-deleted authors, e.g., for admin panels)    
    public void displayAllIncludeDeletedAuthors() {

        try {
            SoftDeleteContext.setShowDeleted(true); // switches parameter extraction state globally

            List<Author> authors = authorRepository.findAll();

            System.out.println("\nAll authors including deleted:");
            authors.forEach(a -> System.out.println("Author name: " + a.getName() + " deleted " + a.getDeleted()));
            System.out.println();
        } finally {
            SoftDeleteContext.clear(); // safely resets back to default isolation state
        }
    }

    // returns only active authors because includeDeleted defaults to false
    public void displayAllExceptDeletedAuthors() {

        List<Author> authors = authorRepository.findAll();

        System.out.println("\nAll authors except deleted:");
        authors.forEach(a -> System.out.println("Author name: " + a.getName() + " deleted " + a.getDeleted()));
        System.out.println();
    }

    public void displayAllOnlyDeletedAuthors() {

        try {
            SoftDeleteContext.setShowOnlyDeleted(true); // switches parameter extraction state globally            

            List<Author> authors = authorRepository.findAll();

            System.out.println("\nAll deleted authors:");
            authors.forEach(a -> System.out.println("Author name: " + a.getName() + " deleted " + a.getDeleted()));
            System.out.println();
        } finally {
            SoftDeleteContext.clearOnlyDeleted(); // safely resets back to default isolation state            
        }
    }

    @Transactional
    public void softDeleteBook() {
        Author author = authorRepository.findById(4L).get();  // bypass the filter
        Book book = author.getBooks().get(0);  // use the filter

        author.removeBook(book);
    }

    // returns everything (both active and soft-deleted books, e.g., for admin panels)    
    public void displayAllIncludeDeletedBooks() {

        try {
            SoftDeleteContext.setShowDeleted(true); // switches parameter extraction state globally

            List<Book> books = bookRepository.findAll();

            System.out.println("\nAll books including deleted:");
            books.forEach(b -> System.out.println("Book title: " + b.getTitle() + " deleted " + b.getDeleted()));
            System.out.println();
        } finally {
            SoftDeleteContext.clear(); // safely resets back to default isolation state
        }
    }

    // returns only active books because includeDeleted defaults to false
    public void displayAllExceptDeletedBooks() {
        List<Book> books = bookRepository.findAll();

        System.out.println("\nAll books except deleted:");
        books.forEach(b -> System.out.println("Book title: " + b.getTitle() + " deleted " + b.getDeleted()));
        System.out.println();
    }

    public void displayAllOnlyDeletedBooks() {
        try {
            SoftDeleteContext.setShowOnlyDeleted(true); // switches parameter extraction state globally            

            List<Book> books = bookRepository.findAll();

            System.out.println("\nAll deleted books:");
            books.forEach(b -> System.out.println("Book title: " + b.getTitle() + " deleted " + b.getDeleted()));
            System.out.println();
        } finally {
            SoftDeleteContext.clearOnlyDeleted(); // safely resets back to default isolation state            
        }
    }

    @Transactional
    public void restoreBook() {

        try {
            SoftDeleteContext.setRestoreDeleted(true); // switches parameter extraction state globally

            bookRepository.restoreById(1L);
        } finally {
            SoftDeleteContext.clearRestoreDeleted();// safely resets back to default isolation state            
        }
    }

    @Transactional
    public void restoreAuthor() {

        try {
            SoftDeleteContext.setRestoreDeleted(true); // switches parameter extraction state globally

            authorRepository.restoreById(1L);
            bookRepository.restoreByAuthorId(1L);
        } finally {
            SoftDeleteContext.clearRestoreDeleted();// safely resets back to default isolation state            
        }
    }
}
