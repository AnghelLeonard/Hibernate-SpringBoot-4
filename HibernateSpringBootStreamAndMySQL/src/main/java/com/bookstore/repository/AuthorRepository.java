package com.bookstore.repository;

import com.bookstore.entity.Author;
import jakarta.persistence.QueryHint;
import java.util.stream.Stream;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a")
    @QueryHints(value = @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    Stream<Author> streamAll();           
    
    /* use this method if you add to JDBC URL the useCursorFetch=true setting
    @Query("SELECT a FROM Author a")
    @QueryHints(value = @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "30"))
    Stream<Author> streamAll();
    */
}
