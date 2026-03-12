package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    // not efficient
    @Transactional
    public void deleteViaCascadeRemove() {
        Author author = authorRepository.findByName("Joana Nimar");

        authorRepository.delete(author);
        // authorRepository.deleteById(author.getId());
    }

    // not efficient
    @Transactional
    public void deleteViaOrphanRemoval() {
        Author author = authorRepository.findByNameWithBooks("Joana Nimar");

        author.removeBooks();
        authorRepository.delete(author);
        // authorRepository.deleteById(author.getId());
    }

    // One Author is loaded in the Persistent Context (no books)
    @Transactional
    public void deleteViaIdentifiers() {
        Author author = authorRepository.findByName("Joana Nimar");

        bookRepository.deleteByAuthorIdentifier(author.getId());
        authorRepository.deleteByIdentifier(author.getId());
        // authorRepository.deleteAllInBatch(List.of(author));                      
    }

    // More Author are loaded in the Persistent Context (no books)
    @Transactional
    public void deleteViaBulkIn() {
        List<Author> authors = authorRepository.findByAge(34);

        bookRepository.deleteBulkByAuthors(authors);
        authorRepository.deleteAllInBatch(authors);
    }

    // One/more Author and the associated Book are in Persistent Context
    @Transactional
    public void deleteViaDeleteAllInBatch() {
        List<Author> authors = authorRepository.findByGenreWithBooks("Anthology");

        List<Book> books = new ArrayList<>();
        for (Author author : authors) {
            books.addAll(author.getBooks());
        }

        bookRepository.deleteAllInBatch(books);
        authorRepository.deleteAllInBatch(authors);
    }      

    // No Author or Book that should be deleted are loaded in the Persistent Context
    @Transactional
    public void deleteViaHardCodedIdentifiers() {
        bookRepository.deleteByAuthorIdentifier(4L);
        authorRepository.deleteByIdentifier(4L);
    }

    // No Author or Book that should be deleted are loaded in the Persistent Context
    @Transactional
    public void deleteViaBulkHardCodedIdentifiers() {
        List<Long> authorsIds = Arrays.asList(1L, 4L);

        bookRepository.deleteBulkByAuthorIdentifier(authorsIds);
        authorRepository.deleteAllByIdInBatch(authorsIds);
    }    
}
