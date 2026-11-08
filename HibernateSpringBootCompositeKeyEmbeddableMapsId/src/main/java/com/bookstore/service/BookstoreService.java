package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.AuthorId;
import com.bookstore.entity.Book;
import com.bookstore.entity.Publisher;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.repository.PublisherRepository;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository,
            PublisherRepository publisherRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public Long addPublisher() {
        Publisher publisher = new Publisher();
        publisher.setName("GreatBooks Ltd");
        publisher.setUrc(92284434);

        return publisherRepository.save(publisher).getId();
    }

    @Transactional
    public void addAuthorsWithBooks(Long publisherId) {

        Publisher publisherRef= publisherRepository.getReferenceById(publisherId);

        Author author1 = new Author();
        author1.setId(new AuthorId(publisherRef.getId(), "Alicia Tom"));
        author1.setPublisher(publisherRef);
        author1.setGenre("Anthology");

        Author author2 = new Author();
        author2.setId(new AuthorId(publisherRef.getId(), "Joana Nimar"));
        author2.setPublisher(publisherRef);
        author2.setGenre("History");

        Author author3 = new Author();
        author3.setId(new AuthorId(publisherRef.getId(), "Mark Janel"));
        author3.setPublisher(publisherRef);
        author3.setGenre("Anthology");

        Book book1 = new Book();
        book1.setIsbn("001-AT");
        book1.setTitle("The book of swords");

        Book book2 = new Book();
        book2.setIsbn("002-AT");
        book2.setTitle("Anthology of a day");

        Book book3 = new Book();
        book3.setIsbn("003-AT");
        book3.setTitle("Anthology today");

        Book book4 = new Book();
        book4.setIsbn("001-MJ");
        book4.setTitle("Anthology of 2022");

        author1.addBook(book1); // use addBook() helper
        author1.addBook(book2);
        author2.addBook(book3);
        author3.addBook(book4);

        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
    }

    public void fetchAuthorByName() {
        Author author = authorRepository.fetchByName("Alicia Tom");

        System.out.println(author);
    }

    @Transactional
    public void removeBookOfAuthor() {

        Author author = authorRepository.fetchWithBooks(92284434, "Alicia Tom");

        author.removeBook(author.getBooks().get(0));
    }

    public void removeBookOfAuthorOptimal() {

        bookRepository.deleteBookByAuthor("001-AT", 92284434, "Alicia Tom");
    }

    @Transactional
    public void removeAuthor() {
        Publisher publisher = publisherRepository.findByUrc(92284434);
        authorRepository.deleteById(new AuthorId(publisher.getId(), "Alicia Tom"));
    }

    @Transactional
    public void removeAuthorOptimal() {
        Author author = authorRepository.fetchWithBooks(92284434, "Joana Nimar");
        authorRepository.delete(author);
    }

    public void removeAuthorBulk() {
        authorRepository.deleteByPublisherUrcAndName(92284434, "Mark Janel");
    }
}
