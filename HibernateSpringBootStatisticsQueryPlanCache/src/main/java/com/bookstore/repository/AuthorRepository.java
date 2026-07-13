package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    @Query(value = "SELECT a, b FROM Author a INNER JOIN a.books b WHERE b.price < ?1")
    List<Author> fetchAuthorsBooksByPriceInnerJoin(int price);
    
    @Query(value = "SELECT a FROM Author a JOIN FETCH a.books b WHERE b.price > ?1")
    List<Author> fetchAuthorsBooksByPriceJoinFetch(int price);
    
    @Query("SELECT a FROM Author a WHERE a.age > ?1")
    List<Author> fetchByAge(int age);        
}
