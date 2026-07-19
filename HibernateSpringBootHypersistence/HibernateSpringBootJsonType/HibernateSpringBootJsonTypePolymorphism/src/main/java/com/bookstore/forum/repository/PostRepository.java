package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Finds every post whose {@code body} array contains at least one element
     * matching the given JSON fragment, using PostgreSQL's {@code jsonb}
     * containment operator {@code @>}. For example, the fragment
     * {@code [{"type":"code","language":"java"}]} matches any post that has a
     * Java code block. This predicate is exactly what a GIN index on the
     * {@code body} column accelerates.
     */
    @Query(value = "SELECT * FROM polymorphic_post p WHERE p.body @> CAST(:fragment AS jsonb)",
        nativeQuery = true)
    List<Post> findByBodyContaining(@Param("fragment") String fragment);
}
