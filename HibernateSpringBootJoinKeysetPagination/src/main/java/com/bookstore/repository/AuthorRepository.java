package com.bookstore.repository;

import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("""
        SELECT new com.bookstore.dto.AuthorBookFlatDto(
            a.id, a.name, a.age, b.id, b.title, b.isbn
        )
        FROM Author a
        JOIN a.books b
        WHERE a.genre = :genre
        ORDER BY a.id ASC, b.id ASC
    """)
    List<AuthorBookFlatDto> fetchFirstPageOfDto(@Param("genre") String genre, Limit limit);

    @Query("""
        SELECT new com.bookstore.dto.AuthorBookFlatDto(
            a.id, a.name, a.age, b.id, b.title, b.isbn
        )
        FROM Author a
        JOIN a.books b
        WHERE a.genre = :genre
          AND (a.id, b.id) > (:lastAuthorId, :lastBookId)
        ORDER BY a.id ASC, b.id ASC
    """)
    List<AuthorBookFlatDto> fetchNextPageOfDto(
            @Param("genre") String genre,
            @Param("lastAuthorId") Long lastAuthorId,
            @Param("lastBookId") Long lastBookId,
            Limit limit);

    @Query("""
        SELECT a.id, a.name, a.age, b.id, b.title, b.isbn        
        FROM Author a
        JOIN a.books b
        WHERE a.genre = :genre
        ORDER BY a.id ASC, b.id ASC
    """)
    List<Object[]> fetchFirstPageOfRaw(@Param("genre") String genre, Limit limit);
    
    @Query("""
        SELECT a.id, a.name, a.age, b.id, b.title, b.isbn        
        FROM Author a
        JOIN a.books b
        WHERE a.genre = :genre
          AND (a.id, b.id) > (:lastAuthorId, :lastBookId)
        ORDER BY a.id ASC, b.id ASC
    """)
    List<Object[]> fetchNextPageOfRaw(
            @Param("genre") String genre,
            @Param("lastAuthorId") Long lastAuthorId,
            @Param("lastBookId") Long lastBookId,
            Limit limit);
}
