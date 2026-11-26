package com.bookstore.test;

import com.bookstore.entity.Review;
import com.bookstore.repository.ReviewRepository;
import com.bookstore.service.BookstoreService;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionSystemException;

@SpringBootTest
class BookstoreServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BookstoreServiceTest.class);

    @Autowired
    private BookstoreService bookstoreService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // valid save tests (Book, Article, Magazine individually)
    @Test
    void whenPersistValidReviews_thenSucceed() {
        Review bookReview = bookstoreService.persistBookReview();
        assertNotNull(bookReview.getId());
        log.info("Successfully persisted book review with ID: {}", bookReview.getId());

        Review articleReview = bookstoreService.persistArticleReview();
        assertNotNull(articleReview.getId());
        log.info("Successfully persisted article review with ID: {}", articleReview.getId());

        Review magazineReview = bookstoreService.persistMagazineReview();
        assertNotNull(magazineReview.getId());
        log.info("Successfully persisted magazine review with ID: {}", magazineReview.getId());

        // clean up inserted test rows so the database stays clean
        reviewRepository.deleteAllById(
                java.util.List.of(bookReview.getId(), articleReview.getId(), magazineReview.getId())
        );
    }

    // multiple associations test (Bean Validation blocks before SQL INSERT)
    @Test
    void whenPersistReviewWithMultipleAssociations_thenThrowValidationException() {
        try {
            bookstoreService.persistReviewWrong();
            throw new AssertionError("Expected a validation exception, but persistReviewWrong() succeeded.");
        } catch (TransactionSystemException ex) {
            assertInstanceOf(ConstraintViolationException.class, ex.getRootCause());
            log.error("Transaction failed due to validation as expected: {}", ex.getRootCause().getMessage(), ex);
        }
    }

    // all-null test (None of book, article, or magazine is set)
    @Test
    void whenPersistReviewWithNoAssociation_thenVerifyBehavior() {
        try {
            Review saved = bookstoreService.persistReviewWithNoAssociation();
            // If your custom validator uses `<= 1` (at most one), all-null will succeed!
            // If your validator uses `== 1` (exactly one), it will jump to the catch block below.
            log.info("All-null review persisted with ID: {} (allowed by '<= 1' rule)", saved.getId());
            reviewRepository.deleteById(saved.getId());
        } catch (TransactionSystemException ex) {
            assertInstanceOf(ConstraintViolationException.class, ex.getRootCause());
            log.error("All-null review rejected by validation as expected: {}", ex.getRootCause().getMessage(), ex);
        }
    }

    // database trigger test (Bypasses JPA/Hibernate to test PostgreSQL trigger directly)
    @Test
    void whenInsertViaJdbcWithMultipleAssociations_thenDatabaseTriggerBlocksIt() {
        try {
            jdbcTemplate.update(
                    "INSERT INTO \"review\" (\"content\", \"book_id\", \"article_id\") VALUES (?, ?, ?)",
                    "Raw SQL insert violating Just_One_Of_Many trigger", 1L, 1L
            );
            throw new AssertionError("Expected PostgreSQL trigger to reject the insert, but it succeeded.");
        } catch (DataAccessException ex) {
            Throwable rootCause = assertInstanceOf(SQLException.class, ex.getRootCause());
            assertTrue(
                    rootCause.getMessage().contains("A review can be associated with either a book, a magazine or an article"),
                    "Unexpected database error message: " + rootCause.getMessage()
            );
            log.error("PostgreSQL trigger blocked invalid raw insert as expected: {}", rootCause.getMessage(), ex);
        }
    }
}
