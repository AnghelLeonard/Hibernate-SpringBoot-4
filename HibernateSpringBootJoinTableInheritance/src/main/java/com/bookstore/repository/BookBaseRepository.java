package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
@Transactional(readOnly = true)
public interface BookBaseRepository<T extends Book> extends JpaRepository<T, Long> {

    T findByTitle(String title);
    
    @Query(value="SELECT b FROM #{#entityName} AS b WHERE b.isbn = ?1")
    T fetchByIsbn(String isbn);
    
    @Query("""
           SELECT b FROM #{#entityName} AS b 
           JOIN b.author a             
           WHERE a.id = ?1
           """)
    List<T> findEbooksByAuthorId(Long authorId);
}
