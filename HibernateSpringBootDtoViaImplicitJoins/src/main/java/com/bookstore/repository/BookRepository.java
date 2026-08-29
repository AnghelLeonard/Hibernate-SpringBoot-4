package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.projection.AuthorNameBookTitle;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {

    // Fetch books and authors (JPQL)
    @Query(value = "SELECT b.title AS title, b.author.name AS name FROM Book b")
    List<AuthorNameBookTitle> findBooksAndAuthorsJpql();   
}
