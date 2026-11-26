package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Book b WHERE b.author.id = :authorId")
    int deleteAllByAuthorId(@Param("authorId") Long authorId);
}
