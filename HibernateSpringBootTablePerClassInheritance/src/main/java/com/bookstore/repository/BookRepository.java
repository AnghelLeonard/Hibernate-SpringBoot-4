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

    @Query("SELECT b FROM Book b WHERE b.title = ?1")
    Ebook fetchEBookOfTitle(String title);

    @Query("SELECT b FROM Book b WHERE TREAT(b AS Ebook).title = ?1")
    Ebook fetchEBooksOfTitleTreat(String title);
    
    @Query("SELECT b FROM Book b WHERE b.format = ?1")
    Ebook fetchEBookOfFormat(String format);
    
    @Query("SELECT b FROM Book b WHERE TREAT(b AS Ebook).format = ?1")
    Ebook fetchEBooksOfFormatTreat(String format);


}
