package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookstoreService(AuthorRepository authorRepository, BookRepository bookRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void insertAuthorWithBooks() {

        Author author = new Author();
        author.setId(5L);
        author.setName("Alicia Tom");
        author.setAge(38);
        author.setGenre("Anthology");

        Book book = new Book();
        book.setId(5L);
        book.setIsbn("001-AT");
        book.setTitle("The book of swords");

        author.addBook(book); // use addBook() helper
                
        authorRepository.save(author);
    }

    @Transactional
    public void deleteBookOfAuthor() {

        Book book = bookRepository.getReferenceById(1L);
        
        bookRepository.delete(book);
    }
    
    public void deleteAllBooksOfAuthor() {
        bookRepository.deleteAllByAuthorId(2L);
    }
}
