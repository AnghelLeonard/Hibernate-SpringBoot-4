package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.projection.AuthorName;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
       
    // Semi-Join
    @Query(value = """
                   SELECT a.name AS name FROM Author a 
                   WHERE EXISTS (
                     SELECT 1 FROM Book b WHERE b.author.id = a.id AND b.price > 40
                   )
                   """)
    List<AuthorName> findAuthorsHavingBooks();
    
    // Anti-Join
    @Query(value = """
                   SELECT a.name AS name FROM Author a 
                   WHERE NOT EXISTS (
                     SELECT 1 FROM Book b WHERE b.author.id = a.id AND b.price > 40
                   )
                   """)
    List<AuthorName> findAuthorsNoBooks();
}