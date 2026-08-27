package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Transactional(readOnly = true)
    @NativeQuery(value = "SELECT name, age FROM author")
    List<Tuple> fetchAuthors();
}
