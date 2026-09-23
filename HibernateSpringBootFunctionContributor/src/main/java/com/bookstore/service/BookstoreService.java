package com.bookstore.service;

import com.bookstore.dao.Dao;
import java.time.Instant;
import org.springframework.stereotype.Service;
import com.bookstore.repository.BookRepository;
import java.util.List;

@Service
public class BookstoreService {

    private final Dao dao;
    private final BookRepository bookRepository;

    public BookstoreService(Dao dao, BookRepository bookRepository) {
        this.dao = dao;
        this.bookRepository = bookRepository;
    }

    public String callConcat() {
        return bookRepository.callConcat();
    }
    
     public double callCosRadians() {
        return bookRepository.callCosRadians();
    }
    
    public int callField() {
        return bookRepository.callField();        
    }
    
    public List<Integer> callApplyDiscount() {
        return bookRepository.callApplyDiscount();        
    }
    
    public List<String> callApplyDiscountInWhere() {
        return bookRepository.callApplyDiscountInWhere();        
    }
        
    public String titleAndPriceEm() {        
        return dao.fetchTitleAndPrice("$", Instant.now());
    }
    
    public List<String> titleAndPriceGt25Em() {        
        return dao.fetchTitleAndPriceGt25("$", Instant.now());
    }
}
