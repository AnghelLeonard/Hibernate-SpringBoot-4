package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional(readOnly = true)
    @Query(value = """
                   SELECT b FROM Book b WHERE b.price < ?1 AND b.author.id = ?2
                   ORDER BY b.price DESC LIMIT 1
                   """)
    Book fetchNextSmallerPrice(int price, long authorId);
}
