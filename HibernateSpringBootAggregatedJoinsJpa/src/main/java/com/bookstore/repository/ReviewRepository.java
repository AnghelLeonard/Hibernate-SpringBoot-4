package com.bookstore.repository;

import com.bookstore.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    @Query("""
           SELECT r FROM Review r
             LEFT JOIN FETCH r.reviewers                                              
           """)
    List<Review> findReviewsAndReviewers();
}