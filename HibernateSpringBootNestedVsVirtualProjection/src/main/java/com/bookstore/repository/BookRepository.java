package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.dto.BookSummary;
import com.bookstore.dto.SimpleBookSummary;
import com.bookstore.dto.VirtualBookSummary;
import com.bookstore.dto.RecordBookSummary;
import com.bookstore.dto.RecordBookView;
import com.bookstore.dto.SimpleRecordBookSummary;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {

    List<BookSummary> findAllByOrderByRankAsc();
    
    @Query("SELECT b.title AS title, b.rank AS rank, a AS author FROM Book b LEFT JOIN b.author a")
    // or as a INNER JOIN
    // @Query("SELECT b.title AS title, b.rank AS rank, b.author AS author FROM Book b")
    List<BookSummary> findByViaQuery();
   
    @Query("""
           SELECT b.title AS title, b.rank AS rank, a.name AS name, a.genre AS genre 
           FROM Book b LEFT JOIN b.author a
           """)
    List<SimpleBookSummary> findByViaQuerySimpleDto();
    
    @Query("""
           SELECT b.title AS title, b.rank AS rank, a.name AS name, a.genre AS genre
           FROM Book b LEFT JOIN b.author a
           """)
    List<Object[]> findByViaQueryArrayOfObjects(); 
     
    @Query("""
           SELECT b.title AS title, b.rank AS rank, a.name AS name, a.genre AS genre
           FROM Book b LEFT JOIN b.author a
           """)
    List<VirtualBookSummary> findByViaQueryVirtualDto();  
    
    @Query("""
           SELECT b.title AS title, b.rank AS rank, a.name AS name, a.genre AS genre
           FROM Book b LEFT JOIN b.author a
           """)
    List<RecordBookSummary> findByViaQueryRecordDto();
    
    @Query("""
           SELECT new com.bookstore.dto.SimpleRecordBookSummary(b.title, b.rank, 
                  new com.bookstore.dto.SimpleRecordBookSummary$SimpleRecordAuthorSummary(a.name, a.genre))
                  FROM Book b LEFT JOIN b.author a
           """)
    List<SimpleRecordBookSummary> findByViaQuerySimpleRecordDto();
    
    @Query("""
           SELECT new com.bookstore.dto.RecordBookView(b.title, b.rank, 
                  new com.bookstore.dto.RecordBookView$RecordAuthorView(a.name, a.genre))
                  FROM Book b LEFT JOIN b.author a
           """)
    List<RecordBookView> findByViaQuerySimpleRecordView();
}
