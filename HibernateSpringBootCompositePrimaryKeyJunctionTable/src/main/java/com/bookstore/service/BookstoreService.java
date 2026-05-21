package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.entity.AuthorBook;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Book;
import com.bookstore.repository.AuthorBookRepository;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorBookRepository authorBookRepository;

    public BookstoreService(
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            AuthorBookRepository authorBookRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.authorBookRepository = authorBookRepository;
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
        bookOfSwords.setIsbn("001-AT");
        bookOfSwords.setTitle("The book of swords");

        Book oneDay = new Book();
        oneDay.setIsbn("001-MJ");
        oneDay.setTitle("One Day");
        
        Book mistery = new Book();
        mistery.setIsbn("002-MJ");
        mistery.setTitle("Mistery");

        AuthorBook aliciaBook = new AuthorBook(alicia, bookOfSwords);        
        AuthorBook markBook1 = new AuthorBook(mark, oneDay);
        AuthorBook markBook2 = new AuthorBook(mark, mistery);       

        authorRepository.save(alicia);
        bookRepository.save(bookOfSwords);
        authorBookRepository.save(aliciaBook);
        
        authorRepository.save(mark);
        bookRepository.save(oneDay);
        bookRepository.save(mistery);
        authorBookRepository.save(markBook1);
        authorBookRepository.save(markBook2);
    }
}
