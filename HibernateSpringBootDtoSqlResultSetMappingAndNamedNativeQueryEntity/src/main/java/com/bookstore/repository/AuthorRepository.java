package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @NativeQuery(name = "AuthorQuery")
    List<Author> fetchAll();

    /* this currently is not supported */
    // @NativeQuery(name = "AuthorWithBookQuery")
    // List<Object[]> fetchWithBook();
}
