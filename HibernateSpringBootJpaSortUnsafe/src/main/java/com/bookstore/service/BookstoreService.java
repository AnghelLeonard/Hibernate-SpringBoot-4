package com.bookstore.service;

import com.bookstore.entity.Book;
import org.springframework.stereotype.Service;
import com.bookstore.repository.BookRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;    

    public BookstoreService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;        
    }
    
    public List<Book> fetchBookssSortedByEffectivePrice() {
        
        // Sort lowest final price to highest using a mathematical expression
        Sort dynamicSort = JpaSort.unsafe(
            Sort.Direction.ASC, 
            "(price * (1 - discountPercentage / 100))"
        );

        return bookRepository.findAllBooks(dynamicSort);
    }

}
