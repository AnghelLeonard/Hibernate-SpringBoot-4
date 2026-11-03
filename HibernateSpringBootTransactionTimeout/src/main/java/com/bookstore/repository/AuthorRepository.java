package com.bookstore.repository;

import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @QueryHints({
        // @QueryHint(name = "org.hibernate.timeout", value = "10")                // Hibernate timeout in seconds
        // @QueryHint(name = "jakarta.persistence.query.timeout", value = "10000") // JPA timeout in milliseconds
    })
    @NativeQuery(value = "SELECT PG_SLEEP(15)") // for MySQL use sleep(15)
    public void sleepQuery();
}
