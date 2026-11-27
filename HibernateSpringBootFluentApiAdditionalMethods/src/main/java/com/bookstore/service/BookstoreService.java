package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void persistAuthorWithBooks() {

        Author author = new Author()
                .withId(1L)
                .withName("Joana Nimar")
                .withAge(34)
                .withGenre("History")
                .addBook(new Book()
                        .withId(1L)
                        .withTitle("A History of Ancient Prague")
                        .withIsbn("001-JN"))
                .addBook(new Book()
                        .withId(2L)
                        .withTitle("A People's History")
                        .withIsbn("002-JN"));

        authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public void displayAuthorWithBooks() {

        Author author = authorRepository.findByName("Joana Nimar");

        System.out.println(author + "  Books: " + author.getBooks());
    }
}
