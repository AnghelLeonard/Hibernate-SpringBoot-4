package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(
        value = """
            select p.id
            from post p
            where json_unquote(json_extract(p.properties, '$.flair')) = :flair
            """,
        nativeQuery = true
    )
    List<Long> findIdsByFlair(@Param("flair") String flair);
}
