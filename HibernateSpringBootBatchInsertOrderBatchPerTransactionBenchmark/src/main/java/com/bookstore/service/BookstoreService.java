package com.bookstore.service;

import java.util.ArrayList;
import java.util.List;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    long pka = 0;
    long pkb = 0; 
    public void batch5AuthorsAnd5Books() {

        List<Author> authors = new ArrayList<>();
               
        for (int i = 0; i < 5; i++) {

            Author author = new Author();
            author.setId(++pka);
            author.setName("Name_" + i);
            author.setGenre("Genre_" + i);
            author.setAge(18 + i);

            for (int j = 0; j < 5; j++) {
                Book book = new Book();
                book.setId(++pkb);
                book.setTitle("Title: " + j);
                book.setIsbn("Isbn: " + j);

                author.addBook(book);
            }

            authors.add(author);
        }

        authorRepository.saveInBatch(authors);
    }
    
    public void batch25AuthorsAnd5Books() {

        List<Author> authors = new ArrayList<>();
               
        for (int i = 0; i < 25; i++) {

            Author author = new Author();
            author.setId(++pka);
            author.setName("Name_" + i);
            author.setGenre("Genre_" + i);
            author.setAge(18 + i);

            for (int j = 0; j < 5; j++) {
                Book book = new Book();
                book.setId(++pkb);
                book.setTitle("Title: " + j);
                book.setIsbn("Isbn: " + j);

                author.addBook(book);
            }

            authors.add(author);
        }

        authorRepository.saveInBatch(authors);
    }
    
    public void batch50AuthorsAnd5Books() {

        List<Author> authors = new ArrayList<>();
               
        for (int i = 0; i < 50; i++) {

            Author author = new Author();
            author.setId(++pka);
            author.setName("Name_" + i);
            author.setGenre("Genre_" + i);
            author.setAge(18 + i);

            for (int j = 0; j < 5; j++) {
                Book book = new Book();
                book.setId(++pkb);
                book.setTitle("Title: " + j);
                book.setIsbn("Isbn: " + j);

                author.addBook(book);
            }

            authors.add(author);
        }

        authorRepository.saveInBatch(authors);
    }
    
    public void batch100AuthorsAnd5Books() {

        List<Author> authors = new ArrayList<>();
               
        for (int i = 0; i < 100; i++) {

            Author author = new Author();
            author.setId(++pka);
            author.setName("Name_" + i);
            author.setGenre("Genre_" + i);
            author.setAge(18 + i);

            for (int j = 0; j < 5; j++) {
                Book book = new Book();
                book.setId(++pkb);
                book.setTitle("Title: " + j);
                book.setIsbn("Isbn: " + j);

                author.addBook(book);
            }

            authors.add(author);
        }

        authorRepository.saveInBatch(authors);
    }
    
    public void batch500AuthorsAnd5Books() {

        List<Author> authors = new ArrayList<>();
               
        for (int i = 0; i < 500; i++) {

            Author author = new Author();
            author.setId(++pka);
            author.setName("Name_" + i);
            author.setGenre("Genre_" + i);
            author.setAge(18 + i);

            for (int j = 0; j < 5; j++) {
                Book book = new Book();
                book.setId(++pkb);
                book.setTitle("Title: " + j);
                book.setIsbn("Isbn: " + j);

                author.addBook(book);
            }

            authors.add(author);
        }

        authorRepository.saveInBatch(authors);
    }
}
