package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.id.BookId;
import org.springframework.stereotype.Service;
import com.bookstore.repository.BookRepository;
import java.util.List;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;

    public BookstoreService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Long insertBook() {
        
        Book book = new Book();
        book.setTitle("Anthology of 2022");
        book.setPublisherId(42L);
        
        Book savedBook = bookRepository.save(book);
        
        // Hibernate populates bookNumber from the database identity column
        Long generatedBookNumber = savedBook.getBookNumber();
        
        System.out.println("Generated book number: " + generatedBookNumber);
        
        return generatedBookNumber;
    }
    
    public Book fetchBook(Long generatedBookNumber) {
        
        BookId id = new BookId(42L, generatedBookNumber);
        
        return bookRepository.findById(id).orElseThrow();
    }
    
    public List<Book> fetchBookByPublisherId() {              
        
        return bookRepository.findByPublisherId(42L);
    }
}
