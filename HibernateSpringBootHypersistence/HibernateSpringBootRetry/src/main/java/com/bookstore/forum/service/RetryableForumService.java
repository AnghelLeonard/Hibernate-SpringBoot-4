package com.bookstore.forum.service;

import io.hypersistence.utils.spring.annotation.Retry;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The retry boundary. {@code @Retry} lives on this bean's method, which is
 * <strong>not</strong> {@code @Transactional} and delegates to the separate
 * {@link PostService} bean &mdash; so the retry wraps the transaction (each
 * attempt is a brand-new transaction), and the delegation crosses a proxy so
 * {@code PostService}'s {@code @Transactional} actually applies.
 *
 * <p>The {@code on} list contains only <strong>recoverable</strong> failures:
 * lock-acquisition timeouts, query timeouts, and connection-acquisition
 * failures &mdash; transient conditions that a fresh attempt can get past. It
 * deliberately excludes {@code OptimisticLockException}: an optimistic conflict
 * is not transient (someone committed a competing change), so retrying it would
 * silently clobber their update. The aspect is generic and would happily retry
 * any {@link Throwable} you list here; the discipline is in choosing the right
 * ones.</p>
 */
@Service
public class RetryableForumService {

    private final PostService postService;

    public RetryableForumService(PostService postService) {
        this.postService = postService;
    }

    // tag::retry[]
    @Retry(times = 5, on = {
        LockTimeoutException.class,
        PessimisticLockException.class,
        LockAcquisitionException.class,
        JDBCConnectionException.class,
        CannotAcquireLockException.class,
        QueryTimeoutException.class,
        DataAccessResourceFailureException.class
    })
    public void incrementLikes(Long id, int delta, AtomicInteger attempts) {
        attempts.incrementAndGet();
        postService.incrementLikes(id, delta);
    }
    // end::retry[]
}
