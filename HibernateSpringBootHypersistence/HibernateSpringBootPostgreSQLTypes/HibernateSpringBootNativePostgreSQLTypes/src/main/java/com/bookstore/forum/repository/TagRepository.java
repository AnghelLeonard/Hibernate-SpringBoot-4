package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Derived-query lookup by name. Even though the column is {@code citext},
     * this is <strong>case-sensitive</strong>: Hibernate has no native
     * {@code citext} type, so it binds the parameter as an ordinary
     * {@code varchar}, and PostgreSQL then compares the {@code citext} column
     * against a {@code text} value case-sensitively. This is exactly where the
     * Hypersistence Utils {@code PostgreSQLCITextType} does better &mdash; it
     * binds the parameter as {@code citext}, so its equivalent lookup is
     * case-insensitive.
     */
    Optional<Tag> findByName(String name);

    /**
     * Case-insensitive lookup that works even with the native mapping, by
     * explicitly casting the bind parameter to {@code citext} so PostgreSQL uses
     * the case-insensitive {@code citext = citext} operator.
     */
    @Query(value = """
        SELECT * FROM native_tag t
        WHERE t.name = CAST(:name AS citext)
        """, nativeQuery = true)
    Optional<Tag> findByNameCaseInsensitively(@Param("name") String name);
}
