package com.bookstore.service;

import com.bookstore.entity.AuthorList;
import com.bookstore.entity.AuthorSet;
import com.bookstore.entity.BookList;
import com.bookstore.entity.BookSet;
import com.bookstore.repository.AuthorListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.repository.AuthorSetRepository;

@Service
public class BookstoreService {

    private final AuthorSetRepository authorSetRepository;
    private final AuthorListRepository authorListRepository;

    public BookstoreService(AuthorSetRepository authorSetRepository,
            AuthorListRepository authorListRepository) {

        this.authorSetRepository = authorSetRepository;
        this.authorListRepository = authorListRepository;
    }

    @Transactional
    public void persistAuthorWithBooksAndRemoveOneBookList() {

        AuthorList alicia = new AuthorList();
        alicia.setId(1L);
        alicia.setName("Alicia Tom");
        alicia.setAge(38);
        alicia.setGenre("Anthology");

        AuthorList mark = new AuthorList();
        mark.setId(2L);
        mark.setName("Mark Janel");
        mark.setAge(23);
        mark.setGenre("Anthology");

        BookList bookOfSwords = new BookList();
        bookOfSwords.setId(1L);
        bookOfSwords.setIsbn("001-AT-MJ");
        bookOfSwords.setTitle("The book of swords");

        BookList oneDay = new BookList();
        oneDay.setId(2L);
        oneDay.setIsbn("002-AT-MJ");
        oneDay.setTitle("One Day");

        BookList headDown = new BookList();
        headDown.setId(3L);
        headDown.setIsbn("001-AT");
        headDown.setTitle("Head Down");

        alicia.addBook(bookOfSwords);
        mark.addBook(bookOfSwords);
        alicia.addBook(oneDay);
        mark.addBook(oneDay);
        alicia.addBook(headDown);

        // having manual IDs will instruct save() to call merge() 
        // and this will trigger extra-SELECT leading to exception
        // so, we use Hypersistence-Utils persist() which avoids the save() anti-pattern
        authorListRepository.persist(alicia);        
        authorListRepository.persistAndFlush(mark);

        System.out.println("================================================");
        System.out.println("Removing a book (List case) ...");
        System.out.println("================================================");

        alicia.removeBook(oneDay);
    }

    @Transactional
    public void persistAuthorWithBooksAndRemoveOneBookSet() {

        AuthorSet alicia = new AuthorSet();
        alicia.setId(1L);
        alicia.setName("Alicia Tom");
        alicia.setAge(38);
        alicia.setGenre("Anthology");

        AuthorSet mark = new AuthorSet();
        mark.setId(2L);
        mark.setName("Mark Janel");
        mark.setAge(23);
        mark.setGenre("Anthology");

        BookSet bookOfSwords = new BookSet();
        bookOfSwords.setId(1L);
        bookOfSwords.setIsbn("001-AT-MJ");
        bookOfSwords.setTitle("The book of swords");

        BookSet oneDay = new BookSet();
        oneDay.setId(2L);
        oneDay.setIsbn("002-AT-MJ");
        oneDay.setTitle("One Day");

        BookSet headDown = new BookSet();
        headDown.setId(3L);
        headDown.setIsbn("001-AT");
        headDown.setTitle("Head Down");

        alicia.addBook(bookOfSwords);
        mark.addBook(bookOfSwords);
        alicia.addBook(oneDay);
        mark.addBook(oneDay);
        alicia.addBook(headDown);

        // having manual IDs will instruct save() to call merge() 
        // and this will trigger extra-SELECT leading to exception
        // so, we use Hypersistence-Utils persist() which avoids the save() anti-pattern
        authorSetRepository.persist(alicia);
        authorSetRepository.persistAndFlush(mark);

        System.out.println("================================================");
        System.out.println("Removing a book (Set case) ...");
        System.out.println("================================================");

        alicia.removeBook(oneDay);
    }
}
