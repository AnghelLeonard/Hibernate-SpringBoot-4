package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {
   
    @NativeQuery(value = "SELECT * FROM book")
    List<Book> findAllIncludingDeleted();

    @NativeQuery(value = "SELECT * FROM book AS b WHERE b.deleted = true")
    List<Book> findAllOnlyDeleted();
    
    @Transactional
    @NativeQuery(value = "UPDATE book SET deleted = false WHERE author_id = ?1")
    @Modifying    
    public void restoreByAuthorId(Long id);
    
    @Transactional
    @NativeQuery(value = "UPDATE book SET deleted = false WHERE id = ?1")
    @Modifying    
    public void restoreById(Long id);
}
