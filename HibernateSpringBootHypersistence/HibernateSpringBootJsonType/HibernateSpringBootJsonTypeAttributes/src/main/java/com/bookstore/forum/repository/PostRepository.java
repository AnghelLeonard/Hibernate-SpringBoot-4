package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Projects a post into a {@link PostSummary} DTO, reading the {@code source}
     * straight out of the {@code raw_payload} JSON column with MySQL's
     * {@code JSON_EXTRACT}/{@code JSON_UNQUOTE} functions.
     */
    @Query(value = """
        select p.title as title,
               json_unquote(json_extract(p.raw_payload, '$.source')) as source
        from post p
        where p.id = :id
        """, nativeQuery = true)
    PostSummary findSummaryById(@Param("id") Long id);
}
