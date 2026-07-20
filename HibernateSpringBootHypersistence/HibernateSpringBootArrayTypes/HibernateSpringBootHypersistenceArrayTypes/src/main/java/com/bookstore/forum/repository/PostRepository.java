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
     * using the PostgreSQL {@code = ANY(array)} operator.
     */
    @Query(value = "SELECT * FROM hypersistence_array_post p WHERE :keyword = ANY(p.keywords)",
        nativeQuery = true)
    List<Post> findByKeyword(@Param("keyword") String keyword);

    /**
     * Finds every post whose {@code keywords} array overlaps the given array,
     * using the PostgreSQL {@code &&} (array overlap) operator.
     */
    @Query(value = "SELECT * FROM hypersistence_array_post p WHERE p.keywords && CAST(:keywords AS text[])",
        nativeQuery = true)
    List<Post> findByAnyKeyword(@Param("keywords") String[] keywords);
}
