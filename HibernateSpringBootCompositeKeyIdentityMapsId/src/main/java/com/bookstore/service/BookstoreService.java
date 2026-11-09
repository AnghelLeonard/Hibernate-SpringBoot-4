package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Publisher;
import com.bookstore.id.BookId;
import org.springframework.stereotype.Service;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.PublisherRepository;
import java.util.List;

@Service
public class BookstoreService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public BookstoreService(PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }
    
    public record BookResult(Publisher publisher, Long generatedNumber) {}

    public BookResult insertBook() {
        
        Publisher publisher = new Publisher();
        publisher.setName("Cool Publications");
        
        Publisher savedPublisher = publisherRepository.save(publisher);
               
        Book book = new Book();
        book.setTitle("Anthology of 2022");
        book.setPublisher(savedPublisher);
        
        Book savedBook = bookRepository.save(book);
        
        // Hibernate populates bookNumber from the database identity column
        Long generatedBookNumber = savedBook.getBookNumber();
        
        System.out.println("Generated book number: " + generatedBookNumber);
        
        return new BookResult(publisher, generatedBookNumber);
    }
    
    public Book fetchBook(BookResult bookResult) {
        
        BookId id = new BookId(bookResult.publisher.getId(), bookResult.generatedNumber);
        
        return bookRepository.findById(id).orElseThrow();
    }     
}
