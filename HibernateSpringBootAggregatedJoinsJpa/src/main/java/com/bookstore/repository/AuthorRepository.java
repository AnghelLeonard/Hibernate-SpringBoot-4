package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
        
    @Query("""
           SELECT a FROM Author a
             LEFT JOIN FETCH a.books
             ORDER BY a.id
           """)
    List<Author> findAuthorsAndBooks();        

@Query("""
           SELECT a FROM Author a
             LEFT JOIN FETCH a.tags          
           """)
    List<Author> findAuthorsAndTags();    
}
