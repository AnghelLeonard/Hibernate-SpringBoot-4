package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostStatus;
import com.bookstore.forum.repository.PostJpaRepository;
import com.bookstore.forum.repository.PostRepository;
import com.bookstore.forum.repository.PostSummary;
import jakarta.persistence.LockModeType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The <strong>recommended</strong> service. It exercises the explicit
 * {@link PostRepository} ({@code BaseJpaRepository}) contract &mdash;
 * {@code persist} for new rows, {@code persistAll} for a batched insert,
 * {@code update} for a direct UPDATE with no preceding SELECT, and
 * {@code lockById} for a pessimistic lock &mdash; and answers
 * "the most-viewed approved posts" with a single SQL statement that filters,
 * orders, limits and projects on the database side.
 *
 * <p>{@link com.bookstore.forum.service.AntiPatternForumService} extends this
 * class and overrides {@link #findMostViewedAndApprovedPosts(int)} with the
 * {@code findAll()} anti-pattern, so the two can be injected side by side and
 * compared. This class is {@link Primary} so that ordinary type-based injection
 * always resolves to the correct implementation.</p>
 */
@Service
@Primary
public class ForumService {

    protected final PostRepository postRepository;
    protected final PostJpaRepository postJpaRepository;

    public ForumService(PostRepository postRepository, PostJpaRepository postJpaRepository) {
        this.postRepository = postRepository;
        this.postJpaRepository = postJpaRepository;
    }

    // tag::good-findmostviewed[]
    /**
     * The database does the filter, the ordering, the limit and the projection;
     * only the rows the caller asked for ever cross the wire.
     */
    @Transactional(readOnly = true)
    public List<PostSummary> findMostViewedAndApprovedPosts(int limit) {
        return postJpaRepository.findByStatusOrderByViewsDesc(
            PostStatus.APPROVED, PageRequest.of(0, limit));
    }
    // end::good-findmostviewed[]

    @Transactional
    public Post persist(Post post) {
        return postRepository.persist(post);
    }

    @Transactional
    public List<Post> persistAll(List<Post> posts) {
        return postRepository.persistAll(posts);
    }

    /**
     * Direct UPDATE with no SELECT, even though {@code post} is detached &mdash;
     * {@code update} delegates to a {@code StatelessSession.update}.
     */
    @Transactional
    public Post update(Post post) {
        return postRepository.update(post);
    }

    @Transactional
    public Post lockById(Long id, LockModeType lockMode) {
        return postRepository.lockById(id, lockMode);
    }

    @Transactional(readOnly = true)
    public List<Post> findByTitle(String title) {
        return postRepository.findByTitle(title);
    }
}
