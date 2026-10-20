package com.bookstore.repository;

import com.bookstore.entity.Ebook;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface EbookRepository extends BookBaseRepository<Ebook> {
    
    List<Ebook> findByFormat(String format);
    
    @Query("SELECT b FROM Ebook b WHERE b.format = ?1")
    List<Ebook> fetchByFormat(String format);
    
    @Query("SELECT b,a FROM Ebook b JOIN b.author a")
    List<Ebook> fetchEbooksAndAuthorsJ();

    @Query("SELECT b FROM Ebook b JOIN FETCH b.author")
    List<Ebook> fetchEbooksAndAuthorsJF();
}
