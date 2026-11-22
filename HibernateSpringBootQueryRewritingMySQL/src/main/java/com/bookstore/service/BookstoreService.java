package com.bookstore.service;

import com.bookstore.entity.Book;
import org.springframework.stereotype.Service;
import com.bookstore.repository.BookRepository;
import com.bookstore.rewriter.FullTextQueryRewriter;
import java.util.List;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;

    public BookstoreService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> searchBooks(String keyword, FullTextQueryRewriter.SearchMode mode) {
        try {
            FullTextQueryRewriter.setMode(mode);
            return bookRepository.fetchViaFTS(keyword);
        } finally {
            FullTextQueryRewriter.clear();
        }
    }
}
