package com.bookstore.repository;

import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = """
                   SELECT new com.bookstore.dto.AuthorBookFlatDto(
                               a.id, a.name, a.age, b.id, b.title, b.isbn
                           )
                           FROM Author a
                           JOIN a.books b
                           WHERE a.genre = :genre
                             AND (
                                  :lastAuthorId IS NULL OR :lastBookId IS NULL 
                                  OR a.id > :lastAuthorId 
                                  OR (a.id = :lastAuthorId AND b.id > :lastBookId)
                             )
                    ORDER BY a.id ASC, b.id ASC
                   """)                
    List<AuthorBookFlatDto> fetchPageOfDto(String genre, Long lastAuthorId, Long lastBookId, Limit limit); 
    
    @Query(value = """
                   SELECT a.id, a.name, a.age, b.id, b.title, b.isbn
                   FROM Author a
                   JOIN a.books b
                     WHERE a.genre = :genre
                             AND (
                                  :lastAuthorId IS NULL OR :lastBookId IS NULL 
                                  OR a.id > :lastAuthorId 
                                  OR (a.id = :lastAuthorId AND b.id > :lastBookId)
                             )
                   ORDER BY a.id ASC, b.id ASC
                   """)                
    List<Object[]> fetchPageOfRaw(String genre, Long lastAuthorId, Long lastBookId, Limit limit); 
}
