package com.bookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookstore.entity.Author;
import com.bookstore.record.dto.FlatRecordAuthorBook;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.spring.dto.AuthorSummary;
import com.bookstore.spring.dto.SimpleAuthorSummary;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<AuthorSummary> findBy();

    @Query("""
           SELECT a.name AS name, a.genre AS genre, b AS books 
           FROM Author a INNER JOIN a.books b
           """)
    List<AuthorSummary> findByViaQuery();

    @Query("SELECT a FROM Author a JOIN FETCH a.books")
    List<AuthorSummary> findByJoinFetch();

    @Query("""
           SELECT a.name AS name, a.genre AS genre, b.title AS title 
           FROM Author a INNER JOIN a.books b
           """)
    List<SimpleAuthorSummary> findByViaQuerySimpleDto();
    
    @Query("""
           SELECT new com.bookstore.record.dto.FlatRecordAuthorBook(a.name, a.genre, b.title)
           FROM Author a INNER JOIN a.books b
           """)
    List<FlatRecordAuthorBook> findByViaQueryRecordDto();
    
    @Query("""
           SELECT a.name AS name, a.genre AS genre, b.title AS title 
           FROM Author a INNER JOIN a.books b
           """)
    List<Object[]> findByViaArrayOfObjects();
    
    @Query("""
           SELECT a.id AS authorId, a.name AS name, a.genre AS genre,
           b.id AS bookId, b.title AS title FROM Author a INNER JOIN a.books b
           """)
    List<Object[]> findByViaArrayOfObjectsWithIds();
}
