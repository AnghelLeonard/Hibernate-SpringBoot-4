package com.bookstore.forum.repository;

import com.bookstore.forum.entity.Post;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The recommended repository contract. {@link BaseJpaRepository} extends only
 * {@code Repository} + {@code QueryByExampleExecutor}, so it deliberately does
 * <strong>not</strong> inherit {@code save(...)} (neither persist nor update)
 * nor {@code findAll()} (an accidental full table scan). Instead it exposes an
 * explicit contract: {@code persist}/{@code persistAll}, {@code merge}, and
 * {@code update} (a direct UPDATE with no preceding SELECT), plus
 * {@code lockById}.
 *
 * <p>Derived query methods still work exactly as before.</p>
 */
@Repository
public interface PostRepository extends BaseJpaRepository<Post, Long> {

    List<Post> findByTitle(String title);
}
