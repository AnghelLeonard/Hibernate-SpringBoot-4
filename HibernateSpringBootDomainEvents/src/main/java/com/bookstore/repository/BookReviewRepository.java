package com.bookstore.repository;

import com.bookstore.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
        
    BookReview findByEmail(String email);
}
