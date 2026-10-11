package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Page<Author> fetchWithBooksByGenreCQ(int page, int size, Sort sort) {

        return authorRepository.fetchWithBooksByGenreCQ("Anthology",
                PageRequest.of(page, size, sort));
    }

    public Page<Author> fetchWithBooksByGenreEG(int page, int size, Sort sort) {

        return authorRepository.fetchWithBooksByGenreEG("Anthology",
                 PageRequest.of(page, size, sort));
    }

    public Page<Book> fetchWithAuthorsByIsbnCQ(int page, int size, Sort sort) {

        return bookRepository.fetchWithAuthorsByIsbnCQ("001-",
                PageRequest.of(page, size, sort));
    }

    public Page<Book> fetchWithAuthorsByIsbnEG(int page, int size, Sort sort) {

        return bookRepository.fetchWithAuthorsByIsbnEG("001-",
                PageRequest.of(page, size, sort));
    }
}
