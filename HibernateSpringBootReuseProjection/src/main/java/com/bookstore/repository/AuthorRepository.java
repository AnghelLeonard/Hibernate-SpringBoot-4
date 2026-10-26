package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bookstore.dto.AuthorSummary;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<AuthorSummary> findBy();
        
    @Query("""
           SELECT a.age AS age, a.name AS name, a.genre AS genre, 
           a.email AS email, a.address AS address FROM Author a
           """)
    List<AuthorSummary> fetchAll();
    
    @Query("SELECT a.age AS age, a.name AS name, a.genre AS genre FROM Author a")
    List<AuthorSummary> fetchAgeNameGenre();

    @Query("SELECT a.name AS name, a.email AS email FROM Author a")
    List<AuthorSummary> fetchNameEmail();
}
