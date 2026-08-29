package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.projection.AuthorNameBookTitlePrice;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
       
    @Query(value = """
                   SELECT t.title AS title, t.price AS price, a.name AS name
                   FROM Author a INNER JOIN LATERAL (
                     SELECT b.title AS title, b.price AS price 
                     FROM Book b WHERE a.id = b.author.id
                     ORDER BY b.price DESC LIMIT 3
                   ) AS t ORDER BY a.name
                   """)
    List<AuthorNameBookTitlePrice> findAuthorsOfTop3PriceBooks();
}