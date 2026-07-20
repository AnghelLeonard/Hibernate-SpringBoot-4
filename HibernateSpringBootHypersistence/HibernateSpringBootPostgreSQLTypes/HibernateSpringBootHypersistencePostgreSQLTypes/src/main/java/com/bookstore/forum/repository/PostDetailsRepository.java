package com.bookstore.forum.repository;

import com.bookstore.forum.entity.PostDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDetailsRepository extends JpaRepository<PostDetails, Long> {

    /**
     * Finds every thread whose publication {@code tsrange} contains the given
     * timestamp, using the PostgreSQL range containment operator {@code @>}.
     */
    @Query(value = """
        SELECT * FROM post_details p
        WHERE p.publication_period @> CAST(:moment AS timestamp)
        """, nativeQuery = true)
    List<PostDetails> findPublishedAt(@Param("moment") String moment);

    /**
     * Finds every thread whose {@code hstore} attributes define the given key,
     * using the PostgreSQL {@code exist(hstore, text)} function (the {@code ?}
     * operator collides with the JDBC bind placeholder, so the function form is
     * used instead).
     */
    @Query(value = """
        SELECT * FROM post_details p
        WHERE exist(p.attributes, :key)
        """, nativeQuery = true)
    List<PostDetails> findWithAttribute(@Param("key") String key);
}
