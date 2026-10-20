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
    public List<Author> fetchAuthorsWithBooks();
    
    @Query("SELECT a FROM Author a JOIN FETCH a.books b JOIN TREAT(b as Ebook) eb")
    public List<Author> fetchAuthorsWithEbooksTreat(); 
    
    @Query("SELECT a FROM Author a JOIN TREAT(a.books as Ebook)")
    public List<Author> fetchAuthorsNoBooksTreat(); 
    
    @Query("SELECT a FROM Author a JOIN TREAT(a.books as Ebook) eb WHERE eb.format = ?1")
    public List<Author> fetchAuthorsNoBooksFormatTreat(String format); 
    
    @Query("SELECT a FROM Author a JOIN TREAT(a.books as Ebook) eb WHERE eb.title = ?1")
    public List<Author> fetchAuthorsNoBooksTitleTreat(String title);
}
