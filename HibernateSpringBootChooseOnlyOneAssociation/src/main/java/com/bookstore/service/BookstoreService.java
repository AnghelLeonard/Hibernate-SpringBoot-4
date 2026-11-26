package com.bookstore.service;

import com.bookstore.entity.Review;
import com.bookstore.repository.ArticleRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.MagazineRepository;
import org.springframework.stereotype.Service;
import com.bookstore.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class BookstoreService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final ArticleRepository articleRepository;
    private final MagazineRepository magazineRepository;

    public BookstoreService(ReviewRepository reviewRepository, BookRepository bookRepository,
            ArticleRepository articleRepository, MagazineRepository magazineRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.articleRepository = articleRepository;
        this.magazineRepository = magazineRepository;
    }

    @Transactional
    public void persistReviewOk() {

        Review review = new Review();
        review.setId(1L);
        review.setContent("This is a book review ...");
        review.setBook(bookRepository.findById(1L).get());

        reviewRepository.save(review);
    }

    @Transactional
    public Review persistBookReview() {
        Review review = new Review();
        review.setId(2L);
        review.setContent("Great book on Java coding problems.");
        review.setBook(bookRepository.findById(1L).orElseThrow());
        return reviewRepository.save(review);
    }

    @Transactional
    public Review persistArticleReview() {
        Review review = new Review();
        review.setId(3L);
        review.setContent("Helpful CSS3 tricks.");
        review.setArticle(articleRepository.findById(1L).orElseThrow());
        return reviewRepository.save(review);
    }

    @Transactional
    public Review persistMagazineReview() {
        Review review = new Review();
        review.setId(4L);
        review.setContent("Informative Java QA issue.");
        review.setMagazine(magazineRepository.findById(1L).orElseThrow());
        return reviewRepository.save(review);
    }

    @Transactional
    public Review persistReviewWithNoAssociation() {
        Review review = new Review();
        review.setId(5L);
        review.setContent("Orphan review with no book, article, or magazine.");
        return reviewRepository.save(review);
    }
    
    @Transactional
    public void persistReviewWrong() {
        Review review = new Review();
        review.setId(6L);
        review.setContent("This is an article and magazine review ...");
        review.setArticle(articleRepository.findById(1L).orElseThrow());

        // This sets a second association, which violates the "just one of many" rule
        review.setMagazine(magazineRepository.findById(1L).orElseThrow());

        reviewRepository.save(review);
    }
}
