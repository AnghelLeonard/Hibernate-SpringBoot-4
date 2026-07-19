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
     * Finds every post whose {@code keywords} array contains the given value,
     * using the PostgreSQL {@code = ANY(array)} operator. The array column is
     * mapped natively, but the query operators are identical to the
     * Hypersistence Utils variant.
     */
    @Query(value = "SELECT * FROM native_array_post p WHERE :keyword = ANY(p.keywords)",
        nativeQuery = true)
    List<Post> findByKeyword(@Param("keyword") String keyword);
}
