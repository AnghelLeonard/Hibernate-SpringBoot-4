package com.bookstore.service;

import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.Paperback;
import com.bookstore.entity.Ebook;
import com.bookstore.repository.EbookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final EbookRepository ebookRepository;

    public BookstoreService(AuthorRepository authorRepository, BookRepository bookRepository,
            EbookRepository ebookRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;      
        this.ebookRepository = ebookRepository;      

    }

    public void persistAuthorWithBooks() {

        Author author = new Author();
        author.setName("Alicia Tom");
        author.setAge(38);
        author.setGenre("Anthology");

        Book book = new Book();
        book.setIsbn("001-AT");
        book.setTitle("The book of swords");

        Paperback paperback = new Paperback();
        paperback.setIsbn("002-AT");
        paperback.setTitle("The beatles anthology");
        //paperback.setSizeIn("7.5 x 1.3 x 9.2");
        paperback.setSizeIn(null); // explicit null
        paperback.setWeightLbs("2.7");

        Ebook ebook = new Ebook();
        ebook.setIsbn("003-AT");
        ebook.setTitle("Anthology myths");
        // ebook.setFormat("kindle"); // implicit null

        author.addBook(book); // use addBook() helper
        author.addBook(paperback);
        author.addBook(ebook);

        authorRepository.save(author);
    }

    public void fetchBookByTitle() {
        Book book1 = bookRepository.findByTitle("The book of swords");
        System.out.println(book1); // this is a Book

        Book book2 = bookRepository.findByTitle("The beatles anthology");
        System.out.println(book2); // this is a Paperback               

        Book book3 = bookRepository.findByTitle("Anthology myths");
        System.out.println(book3); // this is an Ebook
    }
    
    public void fetchAllEBooks() {
        List<Ebook> ebooks = ebookRepository.findAll();
        System.out.println(ebooks); 
    }
}
