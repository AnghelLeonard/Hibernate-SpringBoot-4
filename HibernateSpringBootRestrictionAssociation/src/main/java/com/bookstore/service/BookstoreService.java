package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public void fetchAuthorWithAllBooks() {

        Author author = authorRepository.findById(1L).orElseThrow();
        List<Book> books = author.getBooks();

        System.out.println(books);
    }

    @Transactional(readOnly = true)
    public void fetchAuthorWithDiscontinuedBooks() {

        Author author = authorRepository.findById(1L).orElseThrow();
        List<Book> books = author.getAvailableBooks();

        System.out.println(books);
    }

    @Transactional(readOnly = true)
    public void fetchAuthorWithNotDiscontinuedBooks() {

        Author author = authorRepository.findById(1L).orElseThrow();
        List<Book> books = author.getNotAvailableBooks();

        System.out.println(books);
    }
}
