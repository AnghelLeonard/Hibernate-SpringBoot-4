package com.bookstore.service;

import com.bookstore.dto.BookAuthorSearchCriteria;
import com.bookstore.entity.Book;
import com.bookstore.repository.BookAuthorSearchRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final BookAuthorSearchRepository bookSearchRepository;

    public BookstoreService(BookAuthorSearchRepository bookSearchRepository) {
        this.bookSearchRepository = bookSearchRepository;
    }

    public List<Book> searchBooks(String title, String author, boolean sortByTitle) {

        BookAuthorSearchCriteria criteria = new BookAuthorSearchCriteria(title, author, sortByTitle);
        List<Book> results = bookSearchRepository.findBooksDynamically(criteria);

        return results;
    }
}
