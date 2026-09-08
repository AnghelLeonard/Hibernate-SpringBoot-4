package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @EntityGraph(attributePaths = {"books"},
            type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT a FROM Author a ORDER BY a.id")
    List<Author> findAuthorsAndBooks();
    
    @EntityGraph(attributePaths = {"tags"},
            type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT a FROM Author a")
    List<Author> findAuthorsAndTags();
}
