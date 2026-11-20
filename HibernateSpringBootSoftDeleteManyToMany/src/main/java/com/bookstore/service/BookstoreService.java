package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository; 

    public BookstoreService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;      
    }

    @Transactional
    public void insertAuthorsWithBooks() {

        Author alicia = new Author(); 
        alicia.setId(1L);
        alicia.setName("Alicia Tom");
        alicia.setAge(38);
        alicia.setGenre("Anthology");

        Author mark = new Author();       
        mark.setId(2L);
        mark.setName("Mark Janel");
        mark.setAge(23);
        mark.setGenre("Anthology");

        Book bookOfSwords = new Book();  
        bookOfSwords.setId(1L);
        bookOfSwords.setIsbn("001-AT-MJ");
        bookOfSwords.setTitle("The book of swords");

        Book oneDay = new Book();  
        oneDay.setId(2L);
        oneDay.setIsbn("002-AT-MJ");
        oneDay.setTitle("One Day");
        
        Book solo = new Book();    
        solo.setId(3L);
        solo.setIsbn("003-MJ");
        solo.setTitle("Solo");

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
