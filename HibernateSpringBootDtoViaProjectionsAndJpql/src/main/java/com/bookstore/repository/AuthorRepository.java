package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.projection.AuthorNameAge;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    @Query(value = "SELECT a.name AS name, a.age AS age FROM Author a WHERE a.genre=?1")
    List<AuthorNameAge> fetchByGenre1(String genre);
    
    @Query(value = "SELECT a.name AS name, a.age AS age FROM Author a WHERE a.genre=?1 LIMIT ?2")
    List<AuthorNameAge> fetchByGenre2(String genre, int limit);
    
    @Query(value = "SELECT a.name AS name, a.age AS age FROM Author a WHERE a.genre=?1")
    List<AuthorNameAge> fetchByGenre3(String genre, Limit limit);
}
