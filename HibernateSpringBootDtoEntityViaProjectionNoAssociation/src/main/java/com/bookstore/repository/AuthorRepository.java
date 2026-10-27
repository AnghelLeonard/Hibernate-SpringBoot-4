package com.bookstore.repository;

import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.bookstore.dto.Bookstore;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    @Query("SELECT a AS author, b.title AS title FROM Author a JOIN Book b ON a.genre=b.genre ORDER BY a.id")
    List<Bookstore> fetchAll();
}
