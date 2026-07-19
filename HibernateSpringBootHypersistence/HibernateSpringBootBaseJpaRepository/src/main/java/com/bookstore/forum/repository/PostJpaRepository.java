package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The <strong>standard</strong> Spring Data contract, kept only to demonstrate the
 * problem {@link PostRepository} fixes: {@code JpaRepository.save(...)} on a
 * <em>detached</em> entity is a {@code merge}, so it issues a {@code SELECT}
 * followed by an {@code UPDATE} &mdash; a wasted read when the caller already
 * knows the row exists and just wants to update it.
 */
@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {
}
