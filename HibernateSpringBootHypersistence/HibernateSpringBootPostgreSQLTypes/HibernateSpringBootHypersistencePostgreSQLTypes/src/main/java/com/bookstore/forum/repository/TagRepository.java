package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Case-insensitive lookup by name. Because the underlying column is
     * {@code citext}, this derived query matches {@code "Hibernate"} even when
     * asked for {@code "hibernate"} &mdash; no {@code LOWER(...)} needed.
     */
    Optional<Tag> findByName(String name);
}
