package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.Publisher;
import com.bookstore.entity.Review;
import com.bookstore.entity.Reviewer;
import com.bookstore.entity.Tag;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.PublisherRepository;
import com.bookstore.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final PublisherRepository publisherRepository;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository, ReviewRepository reviewRepository,
            PublisherRepository publisherRepository) {

        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public void insertTestData() {

        Set<Reviewer> rws = new HashSet<>();
        List<Review> rs = new ArrayList<>();
        List<Book> bs = new ArrayList<>();
        Set<Tag> ts = new HashSet<>();

        for (int i = 1; i <= 150; i++) {

            Author a = new Author();
            a.setAge(0);
            a.setGenre("Genre_" + i);
            a.setName("Name_" + i);

            bs.clear();
            for (int ii = 1; ii <= 20; ii++) {

                rs.clear();
                Book b = new Book();
                b.setIsbn("Isbn_" + ii);
                b.setTitle("Title_" + ii);

                for (int iii = 1; iii <= 20; iii++) {

                    rws.clear();
                    for (int iv = 1; iv <= 10; iv++) {
                        Reviewer rw = new Reviewer();
                        rw.setReviewerAge(0);
                        rw.setReviewerName("Reviewer_" + iv);
                        rws.add(rw);
                    }

                    Review r = new Review();
                    r.setLanguage("Language_" + iii);
                    r.setScript("Script_" + iii);
                    r.setBook(b);
                    r.setReviewers(rws);
                    rs.add(r);
                }

                b.setReviews(rs);

                Publisher p = new Publisher();
                p.setAddress("Address_" + i);
                p.setCompany("Company_" + i);
                publisherRepository.save(p);

                b.setPublisher(p);
                b.setAuthor(a);
                bs.add(b);
            }

            ts.clear();
            for (int v = 1; v <= 20; v++) {
                Tag t = new Tag();
                t.setTag("Tag_" + v);
                ts.add(t);
            }

            a.setBooks(bs);
            a.setTags(ts);

            authorRepository.save(a);
        }
    }

    @Transactional(readOnly = true)
    public List<Author> fetchAuthorsBooksTagsPublishersReviewersReviews() {

        List<Author> authors = authorRepository.findAuthorsAndBooks();
        authors = authorRepository.findAuthorsAndTags();
        List<Book> books = bookRepository.findBooksAndPublishersAndReviews();
        List<Review> reviews = reviewRepository.findReviewsAndReviewers();

        System.out.println("Authors: " + authors.size());
        System.out.println("Books " + books.size());
        System.out.println("Reviews: " + reviews.size());

        return authors;
    }
}
