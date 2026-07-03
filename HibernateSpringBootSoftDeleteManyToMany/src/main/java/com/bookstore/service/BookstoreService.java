package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
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

    @Transactional
    public void insertAuthorsWithBooks() {

        Author alicia = new Author();       
        alicia.setName("Alicia Tom");
        alicia.setAge(38);
        alicia.setGenre("Anthology");

        Author mark = new Author();       
        mark.setName("Mark Janel");
        mark.setAge(23);
        mark.setGenre("Anthology");

        Book bookOfSwords = new Book();       
        bookOfSwords.setIsbn("001-AT-MJ");
        bookOfSwords.setTitle("The book of swords");

        Book oneDay = new Book();        
        oneDay.setIsbn("002-AT-MJ");
        oneDay.setTitle("One Day");
        
        Book solo = new Book();        
        oneDay.setIsbn("003-MJ");
        oneDay.setTitle("Solo");

        alicia.addBook(bookOfSwords); // use addBook() helper
        alicia.addBook(oneDay);
        mark.addBook(bookOfSwords);
        mark.addBook(oneDay);
        mark.addBook(solo);

        authorRepository.save(alicia);
        authorRepository.save(mark);
    }
    
    @Transactional
    public void softDeleteAuthor() {
        Author author = authorRepository.findById(1L).get();

        authorRepository.delete(author);
    }
    
    @Transactional
    public void softDeleteBook() {
        Author author = authorRepository.findById(2L).get();

        author.removeBooks();
    }
    
    @Transactional
    public void softDeleteAuthorBook() {        

        Author author = authorRepository.findById(1L).get();

        author.removeBooks();
        authorRepository.delete(author);        
    }
}
