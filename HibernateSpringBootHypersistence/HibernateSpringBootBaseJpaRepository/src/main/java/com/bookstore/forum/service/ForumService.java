package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.repository.PostRepository;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Exercises the explicit {@link PostRepository} ({@code BaseJpaRepository})
 * contract: {@code persist} for new rows, {@code persistAll} for a batched
 * insert, {@code update} for a direct UPDATE with no preceding SELECT, and
 * {@code lockById} for a pessimistic lock.
 */
@Service
public class ForumService {

    private final PostRepository postRepository;

    public ForumService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

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
