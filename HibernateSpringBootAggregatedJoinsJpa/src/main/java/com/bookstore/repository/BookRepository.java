package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface BookRepository extends JpaRepository<Book, Long> {      
    
    @Query("""
           SELECT b FROM Book b             
             LEFT JOIN FETCH b.publisher
             LEFT JOIN FETCH b.reviews
           """)
    List<Book> findBooksAndPublishersAndReviews();
}
