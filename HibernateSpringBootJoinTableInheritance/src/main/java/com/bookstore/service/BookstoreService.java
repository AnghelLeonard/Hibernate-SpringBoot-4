package com.bookstore.service;

import com.bookstore.repository.BookRepository;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.Paperback;
import com.bookstore.entity.Ebook;
import com.bookstore.repository.EbookRepository;
import org.springframework.stereotype.Service;
import com.bookstore.repository.PaperbackRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PaperbackRepository paperbackRepository;
    private final EbookRepository ebookRepository;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository,
            PaperbackRepository paperbackRepository,
            EbookRepository ebookRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.paperbackRepository = paperbackRepository;
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
        paperback.setSizeIn("7.5 x 1.3 x 9.2");
        paperback.setWeightLbs("2.7");

        Ebook ebook = new Ebook();
        ebook.setIsbn("003-AT");
        ebook.setTitle("Anthology myths");
        ebook.setFormat("kindle");

        author.addBook(book);
        author.addBook(paperback);
        author.addBook(ebook);

        authorRepository.save(author);
    }

    public void fetchBooksByAuthorId() {
        List<Book> books = bookRepository.fetchBooksByAuthorId(1L);
        System.out.println(books);
    }

    public void fetchAllBooks() {
        List<Book> books = bookRepository.findAll();
        System.out.println(books);
    }

    /*
    @Transactional(readOnly = true)
    public void fetchBookByTitle() {
        Book book = bookRepository.findByTitle("The book of swords");
        System.out.println(book);

        Paperback paperback = (Paperback) bookRepository.findByTitle("The beatles anthology");
        System.out.println(paperback);

        Ebook ebook = (Ebook) bookRepository.findByTitle("Anthology myths");
        System.out.println(ebook);
    }
    
    @Transactional(readOnly = true)
    public void fetchBookByIsbn() {
        Book book = bookRepository.fetchByIsbn("001-AT");
        System.out.println(book);

        Paperback paperback = (Paperback) bookRepository.fetchByIsbn("002-AT");
        System.out.println(paperback);

        Ebook ebook = (Ebook) bookRepository.fetchByIsbn("003-AT");
        System.out.println(ebook);
    }
     */
    @Transactional(readOnly = true)
    public void fetchPaperbackByTitle() {
        // this is a Book
        Paperback p1 = paperbackRepository.findByTitle("The book of swords");
        System.out.println(p1);

        // this is a Paperback
        Paperback p2 = paperbackRepository.findByTitle("The beatles anthology");
        System.out.println(p2);
    }

    @Transactional(readOnly = true)
    public void fetchPaperbackByIsbn() {
        // this is a e-Book
        Paperback p1 = paperbackRepository.fetchByIsbn("003-AT");
        System.out.println(p1);

        // this is a Paperback
        Paperback p2 = paperbackRepository.fetchByIsbn("002-AT");
        System.out.println(p2);
    }

    @Transactional(readOnly = true)
    public void fetchBookByTitle() {
        Book book = bookRepository.findByTitle("The book of swords");
        System.out.println(book);

        Paperback paperback = paperbackRepository.findByTitle("The beatles anthology");
        System.out.println(paperback);

        Ebook ebook = ebookRepository.findByTitle("Anthology myths");
        System.out.println(ebook);
    }

    @Transactional(readOnly = true)
    public void fetchBookByIsbn() {
        Book book = bookRepository.fetchByIsbn("001-AT");
        System.out.println(book);

        Paperback paperback = paperbackRepository.fetchByIsbn("002-AT");
        System.out.println(paperback);

        Ebook ebook = ebookRepository.fetchByIsbn("003-AT");
        System.out.println(ebook);
    }
    
    public void fetchEbookByFormat() {
        List<Ebook> ebooks = ebookRepository.findByFormat("kindle");
        System.out.println(ebooks);
    }

    public void fetchAllEbook() {
        List<Ebook> ebooks = ebookRepository.findAll();
        System.out.println(ebooks);
    }

    public void fetchEbookByFormatQuery() {
        List<Ebook> ebooks = ebookRepository.fetchByFormat("kindle");
        System.out.println(ebooks);
    }

    public void fetchEbooksAndAuthorsJ() {
        List<Ebook> ebooks = ebookRepository.fetchEbooksAndAuthorsJ();
        System.out.println(ebooks + "   |   " + ebooks.get(0).getAuthor());
    }

    public void fetchEbooksAndAuthorsJF() {
        List<Ebook> ebooks = ebookRepository.fetchEbooksAndAuthorsJF();
        System.out.println(ebooks + "   |   " + ebooks.get(0).getAuthor());
    }

    public void fetchEBookOfTitle() {
        Ebook ebook = bookRepository.fetchEBookOfTitle("Anthology myths");
        System.out.println(ebook);
    }

    public void fetchEBooksOfTitleTreat() {
        Ebook ebook = bookRepository.fetchEBooksOfTitleTreat("Anthology myths");
        System.out.println(ebook);
    }

    public void fetchEBookOfFormat() {
        Ebook ebook = bookRepository.fetchEBookOfFormat("kindle");
        System.out.println(ebook);
    }

    public void fetchEBooksOfFormatTreat() {
        Ebook ebook = bookRepository.fetchEBooksOfFormatTreat("kindle");
        System.out.println(ebook);
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAndBooksLazy() {
        Author author = authorRepository.findByName("Alicia Tom");
        List<Book> books = author.getBooks();

        System.out.println(author + "  |  " + books);
        books.forEach(book -> System.out.println(book.getClass()));
    }

    public void fetchAuthorsWithBooks() {
        List<Author> authors = authorRepository.fetchAuthorsWithBooks();
        System.out.println(authors);
        authors.forEach(author -> System.out.println(author.getBooks()));
    }

    public void fetchAuthorsWithEbooksTreat() {
        List<Author> authors = authorRepository.fetchAuthorsWithEbooksTreat();
        System.out.println(authors);
        authors.forEach(author -> System.out.println(author.getBooks()));
    }

    public void fetchAuthorsNoBooksTreat() {
        List<Author> authors = authorRepository.fetchAuthorsNoBooksTreat();
        System.out.println(authors);
        // authors.forEach(author -> System.out.println(author.getBooks())); // LazyInitializationException
    }

    public void fetchAuthorsNoBooksFormatTreat() {
        List<Author> authors = authorRepository.fetchAuthorsNoBooksFormatTreat("kindle");
        System.out.println(authors);
        // authors.forEach(author -> System.out.println(author.getBooks())); // LazyInitializationException
    }
    
    public void fetchAuthorsNoBooksTitleTreat() {
        List<Author> authors = authorRepository.fetchAuthorsNoBooksTitleTreat("Anthology myths");
        System.out.println(authors);
        // authors.forEach(author -> System.out.println(author.getBooks())); // LazyInitializationException
    }
}
