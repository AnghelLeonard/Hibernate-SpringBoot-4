package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The <strong>standard</strong> Spring Data contract, kept only to demonstrate the
 * problem {@link PostRepository} fixes: {@code JpaRepository.save(...)} on a
 * <em>detached</em> entity is a {@code merge}, so it issues a {@code SELECT}
 * followed by an {@code UPDATE} &mdash; a wasted read when the caller already
 * knows the row exists and just wants to update it.
 *
 * <p>It also carries the derived query that the {@code findAll()} anti-pattern is
 * measured against: {@link #findByStatusOrderByViewsDesc(PostStatus, Pageable)}
 * pushes the filter, the ordering, the limit <em>and</em> the projection into a
 * single SQL statement, so the database returns only the rows the caller wants.</p>
 */
@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

    // tag::db-side-query[]
    // WHERE status = ?  ORDER BY views DESC  LIMIT ?, projected to (title, views)
    List<PostSummary> findByStatusOrderByViewsDesc(PostStatus status, Pageable pageable);
    // end::db-side-query[]
}
