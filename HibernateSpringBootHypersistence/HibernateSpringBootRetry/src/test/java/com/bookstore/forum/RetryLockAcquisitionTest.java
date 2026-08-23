package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.service.PostService;
import com.bookstore.forum.service.RetryableForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Two threads contend for a {@code PESSIMISTIC_WRITE} lock, coordinated with a
 * {@link CountDownLatch}:
 *
 * <ul>
 *   <li>Thread A grabs the lock, adds 10 likes, and holds the lock (its
 *       transaction stays open) while it simulates a slow service call.</li>
 *   <li>Thread B calls the {@code @Retry} method to add 1 like. Its early
 *       attempts block on the held lock and fail with a 10&nbsp;ms
 *       <em>lock-acquisition timeout</em> (a recoverable failure). The aspect
 *       retries; once A commits and releases the lock, a later attempt acquires
 *       it and commits.</li>
 * </ul>
 *
 * The negative test shows that without {@code @Retry} the same contention just
 * fails. Each thread logs what it is doing, so the interleaving can be replayed
 * as a sequence diagram.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class RetryLockAcquisitionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryLockAcquisitionTest.class);

    @Autowired
    private PostService postService;

    @Autowired
    private RetryableForumService retryableForumService;

    @BeforeEach
    public void cleanUp() {
        postService.deleteAll();
    }

    // tag::retry-test[]
    @Test
    public void retryRecoversAfterTheBlockingLockIsReleased() throws Exception {
        Long id = postService.createPost("Contended post");
        LOGGER.info("Created Post(id={}) with likes=0", id);

        CountDownLatch lockAcquired = new CountDownLatch(1);
        AtomicInteger attempts = new AtomicInteger();

        ExecutorService pool = Executors.newFixedThreadPool(2, namedThreadFactory());
        try {
            LOGGER.info("Submitting Thread-A (the lock holder)");
            Future<?> holder = pool.submit(
                () -> postService.lockAndHold(id, 10, lockAcquired));

            assertTrue(lockAcquired.await(5, TimeUnit.SECONDS),
                "Thread-A never acquired the lock");
            LOGGER.info("Thread-A holds the lock; submitting Thread-B (the retrier)");

            Future<?> retrier = pool.submit(
                () -> retryableForumService.incrementLikes(id, 1, attempts));

            retrier.get(15, TimeUnit.SECONDS);   // must finish WITHOUT throwing
            holder.get(15, TimeUnit.SECONDS);
        } finally {
            pool.shutdownNow();
        }

        LOGGER.info(
            "Both threads finished; Thread-B needed {} attempts; final likes={}",
            attempts.get(), postService.getLikes(id));
        assertTrue(attempts.get() >= 2,
            "Expected at least one retry, but attempts=" + attempts.get());
        // A added 10, then the retried B added 1
        assertEquals(11, postService.getLikes(id));
    }
    // end::retry-test[]

    @Test
    public void withoutRetryTheContendedUpdateFailsWithALockTimeout() throws Exception {
        Long id = postService.createPost("Contended post");

        CountDownLatch lockAcquired = new CountDownLatch(1);

        ExecutorService pool = Executors.newFixedThreadPool(2, namedThreadFactory());
        try {
            Future<?> holder = pool.submit(() -> postService.lockAndHold(id, 10, lockAcquired));

            assertTrue(lockAcquired.await(5, TimeUnit.SECONDS), "Thread-A never acquired the lock");

            // No @Retry: the single attempt against the held lock times out and fails.
            Future<?> loser = pool.submit(() -> postService.incrementLikes(id, 1));

            ExecutionException failure = assertThrows(ExecutionException.class,
                () -> loser.get(15, TimeUnit.SECONDS));
            assertTrue(isLockAcquisitionFailure(failure.getCause()),
                "Expected a lock-acquisition failure, but was: " + failure.getCause());

            holder.get(15, TimeUnit.SECONDS);
        } finally {
            pool.shutdownNow();
        }

        assertEquals(10, postService.getLikes(id)); // only A committed
    }

    /**
     * Names the pool threads deterministically. Thread A is submitted and its
     * lock is awaited before Thread B is submitted, and A is still running when
     * B is submitted, so the first-created worker is always A and the second B.
     */
    private static ThreadFactory namedThreadFactory() {
        AtomicInteger index = new AtomicInteger();
        return runnable -> new Thread(runnable, index.getAndIncrement() == 0 ? "Thread-A" : "Thread-B");
    }

    private static boolean isLockAcquisitionFailure(Throwable throwable) {
        for (Throwable t = throwable; t != null; t = (t.getCause() == t ? null : t.getCause())) {
            if (t instanceof jakarta.persistence.PessimisticLockException
                || t instanceof jakarta.persistence.LockTimeoutException
                || t instanceof org.hibernate.exception.LockAcquisitionException
                || t.getClass().getSimpleName().toLowerCase().contains("lock")) {
                return true;
            }
        }
        return false;
    }
}
