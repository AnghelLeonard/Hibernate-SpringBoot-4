package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends BookBaseRepository<Book>, JpaSpecificationExecutor<Book> {

    @Query("""
        SELECT b FROM Book b 
        WHERE TREAT(b AS Ebook).format = ?1
    """)
    List<Book> findEBooksOfFormat(String format);
}
