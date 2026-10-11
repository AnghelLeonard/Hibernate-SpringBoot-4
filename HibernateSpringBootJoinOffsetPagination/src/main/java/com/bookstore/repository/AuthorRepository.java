package com.bookstore.repository;

import com.bookstore.dto.AuthorBookCountFlatDto;
import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = """
                   SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
                   FROM Author a 
                   JOIN a.books b WHERE a.genre = ?1
                   """,
            countQuery = """
                   SELECT COUNT(b) 
                   FROM Book b 
                   WHERE b.author IN (SELECT a FROM Author a WHERE a.genre = ?1)                        
                   """)    
            // the Spring Data COUNT query generated here contains JOINs and it represents a performance penalty
            // if we override countQuery to strip JOINs we risk totalElements inconsistencies across pages
            // so pay attention how you decide to override countQuery
    Page<AuthorBookFlatDto> fetchPageOfDto(String genre, Pageable pageable);

    @Query(value = """
                   SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
                   FROM Author a 
                   JOIN a.books b WHERE a.genre = ?1
                   """)
    Slice<AuthorBookFlatDto> fetchSliceOfDto(String genre, Pageable pageable);

    @Query(value = """
                   SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
                   FROM Author a 
                   JOIN a.books b WHERE a.genre = ?1
                   """)
    List<AuthorBookFlatDto> fetchListOfDto(String genre, Pageable pageable);

    @Query(value = """
                   SELECT a.name AS name,a.age AS age,b.title AS title,b.isbn AS isbn,
                          COUNT(*) OVER() AS total
                   FROM Author a
                   JOIN a.books b WHERE a.genre = ?1
                   """)
    List<AuthorBookCountFlatDto> fetchListWithCountOfDto(String genre, Pageable pageable);
       
     @Query("""
        SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
        FROM Author a 
        JOIN a.books b
        WHERE a.id IN (
            SELECT sub.id 
            FROM (
                SELECT a2.id AS id, 
                       DENSE_RANK() OVER (ORDER BY a2.name ASC, a2.id ASC) AS pos
                FROM Author a2
                WHERE a2.genre = ?1
            ) sub
            WHERE sub.pos > ?2 AND sub.pos <= (?2 + ?3)
        )
        ORDER BY a.name ASC, b.title ASC
    """)
    List<AuthorBookFlatDto> fetchListOfDtoDenseRank(String genre, long start, long end); 
    
    @Query("""
            SELECT a.id, a.name, a.age, b.id, b.title, b.isbn
                   FROM Author a 
                   JOIN a.books b
                   WHERE a.id IN (
                       SELECT sub.id 
                       FROM (
                           SELECT a2.id AS id,
                                  DENSE_RANK() OVER (ORDER BY a2.name ASC, a2.id ASC) AS pos
                           FROM Author a2
                           WHERE a2.genre = ?1
                       ) sub
                       WHERE sub.pos > ?2 AND sub.pos <= (?2 + ?3)
                   )
                   ORDER BY a.name ASC, b.title ASC           
    """)
    List<Object[]> fetchListOfRawDenseRank(String genre, long start, long end);      
    
    @Query("""
           WITH RankedAuthors AS (
                       SELECT 
                           a2.id AS authorId,
                           DENSE_RANK() OVER (ORDER BY a2.name ASC, a2.id ASC) AS pos
                       FROM Author a2
                       WHERE a2.genre = ?1
                   ),
                      AggregatedRanks AS (
                          SELECT 
                              ra.authorId AS authorId,
                              ra.pos AS pos,
                              MAX(ra.pos) OVER() AS maxTotalRank
                          FROM RankedAuthors ra
                      )
                      SELECT 
                          a.id, a.name, a.age, b.id, 
                          b.title, b.isbn,
                          ar.maxTotalRank AS maxRank 
                      FROM Author a 
                      JOIN a.books b
                      JOIN AggregatedRanks ar ON a.id = ar.authorId
                      WHERE ar.pos > ?2 AND ar.pos <= (?2 + ?3)
                      ORDER BY a.name ASC, b.title ASC
    """)  
    List<Object[]> fetchListOfRawDenseRankWithCount(String genre, long start, long end);

    @NativeQuery(value = """
        SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
        FROM author a 
        JOIN book b ON a.id = b.author_id
        WHERE a.id IN (
            SELECT sub.id 
            FROM (
                SELECT a2.id AS id, 
                       DENSE_RANK() OVER (ORDER BY a2.name ASC, a2.id ASC) AS pos
                FROM author a2
                WHERE a2.genre = ?1
            ) sub
            WHERE sub.pos > ?2 AND sub.pos <= (?2 + ?3)
        )
        ORDER BY a.name ASC, b.title ASC
    """)
    List<AuthorBookFlatDto> fetchListOfDtoDenseRankNative(String genre, long start, long end);
    
    @Query("SELECT COUNT(a) FROM Author a WHERE a.genre = ?1")
    long countByGenre(String genre);
}
