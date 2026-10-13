package com.bookstore.service;

import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.specification.AuthorSpecification;
import com.bookstore.specification.BookSpecification;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {   

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;   
    
    private final Specification<Author> specAuthor = Specification.unrestricted();
    private final Specification<Author> specAuthorNotDeleted 
            = specAuthor.and(AuthorSpecification.isNotDeleted());
    private final Specification<Author> specAuthorIsDeletedOnly 
            = specAuthor.and(AuthorSpecification.isDeletedOnly());
    
    private final Specification<Book> specBook = Specification.unrestricted();
    private final Specification<Book> specBookNotDeleted 
            = specBook.and(BookSpecification.isNotDeleted());
    private final Specification<Book> specBookIsDeletedOnly 
            = specBook.and(BookSpecification.isDeletedOnly());
    
    public BookstoreService(AuthorRepository authorRepository, BookRepository bookRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;        
    }

    @Transactional
    public void softDeleteAuthor() {
        Author author = authorRepository.findById(1L).get();

        authorRepository.delete(author);
    }
  
    public void displayAllIncludeDeletedAuthors() {                        
        List<Author> authors = authorRepository.findAll();

        System.out.println("\nAll authors including deleted:");
        authors.forEach(a -> System.out.println("Author name: " + a.getName() + " deleted " + a.getDeleted()));
        System.out.println();
    }

    public void displayAllExceptDeletedAuthors() {                
        List<Author> authors = authorRepository.findAll(specAuthorNotDeleted);

        System.out.println("\nAll authors except deleted:");
        authors.forEach(a -> System.out.println("Author name: " + a.getName() + " deleted " + a.getDeleted()));
        System.out.println();
    }

    public void displayAllOnlyDeletedAuthors() {                               
        List<Author> authors = authorRepository.findAll(specAuthorIsDeletedOnly);

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
    
    public void displayAllIncludeDeletedBooks() {                      
        List<Book> books = bookRepository.findAll();

        System.out.println("\nAll books including deleted:");
        books.forEach(b -> System.out.println("Book title: " + b.getTitle() + " deleted " + b.getDeleted()));
        System.out.println();
    }
    
     public void displayAllExceptDeletedBooks() {
        List<Book> books = bookRepository.findAll(specBookNotDeleted);

        System.out.println("\nAll books except deleted:");
        books.forEach(b -> System.out.println("Book title: " + b.getTitle() + " deleted " + b.getDeleted()));
        System.out.println();
    }
    
     public void displayAllOnlyDeletedBooks() {
        List<Book> books = bookRepository.findAll(specBookIsDeletedOnly);

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
