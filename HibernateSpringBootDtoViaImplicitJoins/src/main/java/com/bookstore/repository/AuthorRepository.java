package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.projection.AuthorNameBookTitle;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Fetch authors and books (JPQL)    
    @Query(value = "SELECT element(a.books).title AS title, a.name AS name FROM Author a")   
    List<AuthorNameBookTitle> findAuthorsAndBooksJpql();    
}