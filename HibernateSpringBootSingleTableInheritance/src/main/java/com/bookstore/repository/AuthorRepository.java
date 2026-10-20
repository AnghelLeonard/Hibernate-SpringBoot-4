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
    
    // '2' is the discriminator value for Ebook
    @Query("SELECT a FROM Author a JOIN FETCH a.books b WHERE TYPE(b) = 2")
    public List<Author> fetchAuthorsWithEbooksType(); 
    
    @Query("SELECT a FROM Author a JOIN FETCH a.books b JOIN TREAT(b as Ebook) eb")
    public List<Author> fetchAuthorsWithEbooksTreat();      
}
