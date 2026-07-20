package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;

/**
 * The transactional worker. Each public method runs in its own transaction, so
 * when the retry aspect (on {@link RetryableForumService}) re-invokes the outer
 * method, every attempt gets a <strong>fresh transaction and a fresh entity
 * read</strong> &mdash; the whole point of putting the retry on the service
 * layer.
 */
@Service
public class PostService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Long createPost(String title) {
        return postRepository.save(new Post(title)).getId();
    }

    /**
     * Locks the post with {@code PESSIMISTIC_WRITE} and bumps {@code likes}. A
     * short {@code innodb_lock_wait_timeout} makes a contended lock fail fast
     * with a recoverable timeout instead of blocking for the default 50s.
     */
    @Transactional
    public void incrementLikes(Long id, int delta) {
        entityManager.createNativeQuery("SET SESSION innodb_lock_wait_timeout = 1").executeUpdate();
        Post post = entityManager.find(Post.class, id, LockModeType.PESSIMISTIC_WRITE);
        post.setLikes(post.getLikes() + delta);
        entityManager.flush();
    }

    /**
     * Acquires the {@code PESSIMISTIC_WRITE} lock, bumps {@code likes}, signals
     * {@code lockAcquired}, and then holds the lock (keeping the transaction
     * open) until {@code releaseLock} is counted down &mdash; a controllable
     * blocker for the second thread.
     */
    @Transactional
    public void lockAndHold(Long id, int delta, CountDownLatch lockAcquired, CountDownLatch releaseLock) {
        Post post = entityManager.find(Post.class, id, LockModeType.PESSIMISTIC_WRITE);
        post.setLikes(post.getLikes() + delta);
        entityManager.flush();

        lockAcquired.countDown();
        try {
            releaseLock.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while holding the lock", e);
        }
        // Transaction commits here, releasing the lock.
    }

    @Transactional(readOnly = true)
    public int getLikes(Long id) {
        return entityManager.find(Post.class, id).getLikes();
    }

    @Transactional
    public void deleteAll() {
        postRepository.deleteAllInBatch();
    }
}
