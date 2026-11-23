package com.bookstore.repository;

import com.bookstore.entity.Author;
import com.bookstore.entity.Ebook;
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
    
    // '3' is the discriminator value for Ebook
    @Query("SELECT a FROM Author a JOIN FETCH a.books b WHERE TYPE(b) = Ebook")
    public List<Author> fetchAuthorsWithEbooksType(); 
    
    @Query("SELECT a FROM Author a JOIN FETCH a.books b WHERE TYPE(b) = Ebook AND b.format = 'kindle'")
    public List<Author> fetchAuthorsWithEbooksKindleType(); 
    
    @Query("SELECT a FROM Author a JOIN FETCH a.books b JOIN TREAT(b as Ebook) eb")
    // or SELECT a FROM Author a JOIN FETCH a.books b WHERE TREAT(b AS Ebook) IS NOT NULL
    public List<Author> fetchAuthorsWithEbooksTreat();      
    
    @Query("SELECT a FROM Author a JOIN FETCH a.books b WHERE TREAT(b AS Ebook).format = 'kindle'")
    public List<Author> fetchAuthorsAndEbooksKindleTreat();
    
    @Query("SELECT DISTINCT a FROM Author a JOIN a.books b WHERE TREAT(b AS Ebook).format = 'kindle'")
    public List<Author> fetchAuthorsHavingEbooksKindleTreat();
    
    @Query("SELECT a FROM Author a JOIN FETCH TREAT(a.books AS Ebook)")    
    public List<Author> fetchAuthorsHavingEbooksTreat();
    
    @Query("SELECT a FROM Author a JOIN FETCH TREAT(a.books AS Ebook) b WHERE b.format = 'kindle'")    
    public List<Author> fetchAuthorsJFHavingEbooksKindleTreat();
}
