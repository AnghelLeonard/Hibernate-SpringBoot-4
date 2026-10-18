package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> { 
    
    Author findByName(String name);

    @Query("SELECT a FROM Author a JOIN FETCH a.books b")
    List<Author> findAuthorWithBooks();
    
    @Query("""
           SELECT a FROM Author a
           JOIN FETCH a.books b         
           JOIN TREAT(b as Ebook) eb       
           """)
    List<Author> findAuthorsWithEBooks();

    @Query("""
           SELECT a FROM Author a
           JOIN TREAT(a.books as Ebook) eb       
           WHERE eb.format = ?1
           """)
    List<Author> findAuthorsNoBooksByFormat(String format);
    
    
}
