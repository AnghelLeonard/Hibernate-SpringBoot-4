package com.bookstore.repository;

import com.bookstore.entity.Book;
import com.bookstore.entity.Ebook;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends BookBaseRepository<Book> {

    @Query("SELECT b FROM Book b WHERE b.author.id = ?1")
    List<Book> fetchBooksByAuthorId(Long authorId);
    
    // '2' is the discriminator value for Ebook
    @Query("SELECT b FROM Book b WHERE b.title = ?1 AND TYPE(b) = 2")
    Ebook fetchEBooksOfTitleType(String title);
    
    @Query("SELECT b FROM Book b WHERE TREAT(b AS Ebook).title = ?1")
    Ebook fetchEBooksOfTitleTreat(String title); 
}
