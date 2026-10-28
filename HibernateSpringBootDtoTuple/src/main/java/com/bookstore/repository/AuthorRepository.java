package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    @Query(value = "SELECT a.id AS id, a.name AS name, a.age AS age FROM Author a")
    List<Tuple> fetchAuthorsJpql();
        
    @NativeQuery(value = "SELECT name, age FROM author")
    List<Tuple> fetchAuthorsNative();
}
