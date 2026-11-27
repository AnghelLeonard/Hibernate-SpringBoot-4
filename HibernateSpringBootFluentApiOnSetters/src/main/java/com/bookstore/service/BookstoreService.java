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
                .setId(1L)
                .setName("Joana Nimar")
                .setAge(34)
                .setGenre("History")
                .addBook(new Book()
                        .setId(1L)
                        .setTitle("A History of Ancient Prague")
                        .setIsbn("001-JN"))
                .addBook(new Book()
                        .setId(2L)
                        .setTitle("A People's History")
                        .setIsbn("002-JN"));

        authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public void displayAuthorWithBooks() {

        Author author = authorRepository.findByName("Joana Nimar");

        System.out.println(author + "  Books: " + author.getBooks());
    }
}
