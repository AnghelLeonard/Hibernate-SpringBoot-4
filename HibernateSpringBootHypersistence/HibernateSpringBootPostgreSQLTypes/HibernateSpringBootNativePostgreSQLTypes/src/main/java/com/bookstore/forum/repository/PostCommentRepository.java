package com.bookstore.forum.repository;

import com.bookstore.forum.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    /**
     * Finds every comment whose natively-mapped {@code inet} address falls inside
     * the given subnet (CIDR), using the PostgreSQL {@code <<=} operator. The
     * operator works the same as on the Hypersistence-mapped column &mdash; both
     * resolve to a real {@code inet} column.
     */
    @Query(value = """
        SELECT * FROM native_post_comment c
        WHERE c.remote_address <<= CAST(:subnet AS inet)
        """, nativeQuery = true)
    List<PostComment> findFromSubnet(@Param("subnet") String subnet);
}
