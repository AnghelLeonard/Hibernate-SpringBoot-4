package com.bookstore.service;

import com.bookstore.dto.BookDto;
import com.bookstore.dto.SimpleBookDto;
import com.bookstore.dto.VirtualBookDto;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.repository.BookRepository;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;    

    public BookstoreService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    @Transactional(readOnly = true)
    public List<BookDto> fetchBooksWithAuthorsQueryBuilderMechanism() {
        
        List<BookDto> books = bookRepository.findBy();
        System.out.println("\nResult set:" + books.size());        
        
        return books;
    }

    @Transactional(readOnly = true)
    public List<BookDto> fetchBooksWithAuthorsViaQuery() {
        
        List<BookDto> books = bookRepository.findByViaQuery();
        System.out.println("\nResult set:");

        return books;
    }
    
    @Transactional(readOnly = true)
    public List<SimpleBookDto> fetchBooksWithAuthorsViaQuerySimpleDto() {
        
        List<SimpleBookDto> books = bookRepository.findByViaQuerySimpleDto();
        System.out.println("\nResult set:" + books.size());        

        return books;
    }
    
    @Transactional(readOnly = true)
    public List<VirtualBookDto> fetchBooksWithAuthorsViaQueryVirtualDto() {
        
        List<VirtualBookDto> books = bookRepository.findByViaQueryVirtualDto();
        System.out.println("\nResult set:" + books.size());        

        return books;
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> fetchBooksWithAuthorsViaArrayOfObjects() {
        
        List<Object[]> books = bookRepository.findByViaQueryArrayOfObjects();
        System.out.println("\nResult set:" + books.size());        

        return books;
    }
}
