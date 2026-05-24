package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.repository.BookRepository;
import com.bookstore.entity.Book;
import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookstoreService(BookRepository bookRepository, AuthorRepository authorRepository) {

        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public void fetchBooks() {

        /*
        Book book = bookRepository.findById(7L).orElseThrow();
        Book nextBook = bookRepository.fetchNextSmallerPrice(
                book.getPrice(), book.getAuthor().getId());
        System.out.println("Fetched book with id 7: " + book);
        System.out.println("Fetched book with next smallest price: " + nextBook);
        */
        
        Book book = bookRepository.findById(7L).orElseThrow();
        Book nextBook = book.getNextBook();

        System.out.println("Fetched book with id 7: " + book);
        System.out.println("Fetched book with next smallest price: " + nextBook);
    }
    
     @Transactional(readOnly = true)
     public void fetchCheapestBook() {
         
         Author author = authorRepository.findById(1L).orElseThrow();
         Book cheapestBook = author.getCheapestBook();
         
         System.out.println("Fetched author with id 1: " + author);
         System.out.println("Fetched the cheapest book of this author: " + cheapestBook);
     }
}
