package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    /**
     * How long Thread A keeps the {@code PESSIMISTIC_WRITE} lock while it
     * simulates a slow downstream service call. Its transaction stays open for
     * this long, so the row stays locked.
     */
    public static final long SLOW_SERVICE_MILLIS = 1000;

    /**
     * PostgreSQL {@code lock_timeout} in milliseconds. Thread B waits at most
     * this long for the contended lock before failing with a recoverable
     * timeout &mdash; a value MySQL cannot express, since its
     * {@code innodb_lock_wait_timeout} floor is one second.
     */
    public static final int LOCK_TIMEOUT_MILLIS = 10;

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
     * Thread B's write. It sets a 10&nbsp;ms {@code lock_timeout} so that a
     * contended lock fails fast with a recoverable timeout instead of blocking,
     * then tries to acquire the {@code PESSIMISTIC_WRITE} lock and bump
     * {@code likes}.
     */
    // tag::increment-likes[]
    @Transactional
    public void incrementLikes(Long id, int delta) {
        entityManager.createNativeQuery(
            "SET LOCAL lock_timeout = " + LOCK_TIMEOUT_MILLIS).executeUpdate();
        LOGGER.info("Trying to acquire the lock (lock_timeout={}ms)",
            LOCK_TIMEOUT_MILLIS);

        Post post = entityManager.find(Post.class, id, LockModeType.PESSIMISTIC_WRITE);

        int oldLikes = post.getLikes();
        post.setLikes(oldLikes + delta);
        entityManager.flush();
        LOGGER.info("Lock acquired, likes {} -> {}, committing",
            oldLikes, oldLikes + delta);
    }
    // end::increment-likes[]

    /**
     * Thread A's write. It acquires the {@code PESSIMISTIC_WRITE} lock, bumps
     * {@code likes}, signals {@code lockAcquired}, and then holds the lock by
     * keeping the transaction open while it simulates a slow service call. The
     * lock is released when this method returns and the transaction commits.
     */
    // tag::lock-and-hold[]
    @Transactional
    public void lockAndHold(Long id, int delta, CountDownLatch lockAcquired) {
        LOGGER.info("Acquiring the PESSIMISTIC_WRITE lock");
        Post post = entityManager.find(Post.class, id, LockModeType.PESSIMISTIC_WRITE);
        int oldLikes = post.getLikes();
        post.setLikes(oldLikes + delta);
        entityManager.flush();
        LOGGER.info("Lock acquired, likes {} -> {}", oldLikes, oldLikes + delta);

        lockAcquired.countDown(); // let Thread B start contending for the lock

        LOGGER.info(
            "Holding the lock for {}ms (simulating a slow service call)",
            SLOW_SERVICE_MILLIS
        );
        sleep(SLOW_SERVICE_MILLIS);
        LOGGER.info("Slow service finished, committing and releasing the lock");
        // Transaction commits here, releasing the lock.
    }
    // end::lock-and-hold[]

    @Transactional(readOnly = true)
    public int getLikes(Long id) {
        return entityManager.find(Post.class, id).getLikes();
    }

    @Transactional
    public void deleteAll() {
        postRepository.deleteAllInBatch();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while holding the lock", e);
        }
    }
}
