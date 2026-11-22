package com.bookstore.service;

import com.bookstore.entity.Book;
import org.springframework.stereotype.Service;
import com.bookstore.repository.BookRepository;
import com.bookstore.rewriter.FullTextQueryRewriter;
import java.util.List;
import org.springframework.data.domain.PageRequest;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;

    public BookstoreService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> searchBooks(String keyword, FullTextQueryRewriter.SearchMode mode) {
        try {
            PageRequest pageable = PageRequest.of(1, 3); // page 1, size of 3
            FullTextQueryRewriter.setMode(mode);
            return bookRepository.fetchViaFTS(keyword, pageable);
        } finally {
            FullTextQueryRewriter.clear();
        }
    }
}
