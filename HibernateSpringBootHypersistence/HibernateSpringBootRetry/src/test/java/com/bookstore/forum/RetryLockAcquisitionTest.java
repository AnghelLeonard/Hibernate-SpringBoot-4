package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.service.PostService;
import com.bookstore.forum.service.RetryableForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Two threads contend for a {@code PESSIMISTIC_WRITE} lock, synchronized with
 * {@link CountDownLatch}es:
 *
 * <ul>
 *   <li>Thread A grabs the lock, adds 10 likes, and holds the lock (its
 *       transaction stays open) until the test releases it.</li>
 *   <li>Thread B calls the {@code @Retry} method to add 1 like. Its first
 *       attempt blocks on the held lock and fails with a
 *       <em>lock-acquisition timeout</em> (a recoverable failure). The aspect
 *       retries; once A releases the lock, a later attempt acquires it and
 *       commits.</li>
 * </ul>
 *
 * The negative test shows that without {@code @Retry} the same contention just
 * fails.
 */
@SpringBootTest(properties = "test.database=MYSQL")
@EnabledIfDatabaseAvailable(DatabaseType.MYSQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class RetryLockAcquisitionTest {

    @Autowired
    private PostService postService;

    @Autowired
    private RetryableForumService retryableForumService;

    @BeforeEach
    public void cleanUp() {
        postService.deleteAll();
    }

    @Test
    public void retryRecoversAfterTheBlockingLockIsReleased() throws Exception {
        Long id = postService.createPost("Contended post");

        CountDownLatch lockAcquired = new CountDownLatch(1);
        CountDownLatch releaseLock = new CountDownLatch(1);
        AtomicInteger attempts = new AtomicInteger();

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<?> blocker = pool.submit(() ->
                postService.lockAndHold(id, 10, lockAcquired, releaseLock));

            assertTrue(lockAcquired.await(5, TimeUnit.SECONDS), "Thread A never acquired the lock");

            Future<?> retrier = pool.submit(() ->
                retryableForumService.incrementLikes(id, 1, attempts));

            // Wait until B has failed once and started a later attempt, then let A go.
            awaitCondition(() -> attempts.get() >= 2, 15, TimeUnit.SECONDS);
            releaseLock.countDown();

            retrier.get(15, TimeUnit.SECONDS);   // must finish WITHOUT throwing
            blocker.get(15, TimeUnit.SECONDS);
        } finally {
            releaseLock.countDown();
            pool.shutdownNow();
        }

        assertTrue(attempts.get() >= 2, "Expected at least one retry, but attempts=" + attempts.get());
        assertEquals(11, postService.getLikes(id)); // A added 10, then the retried B added 1
    }

    @Test
    public void withoutRetryTheContendedUpdateFailsWithALockTimeout() throws Exception {
        Long id = postService.createPost("Contended post");

        CountDownLatch lockAcquired = new CountDownLatch(1);
        CountDownLatch releaseLock = new CountDownLatch(1);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<?> blocker = pool.submit(() ->
                postService.lockAndHold(id, 10, lockAcquired, releaseLock));

            assertTrue(lockAcquired.await(5, TimeUnit.SECONDS), "Thread A never acquired the lock");

            // No @Retry: the single attempt against the held lock times out and fails.
            Future<?> loser = pool.submit(() -> postService.incrementLikes(id, 1));

            ExecutionException failure = assertThrows(ExecutionException.class,
                () -> loser.get(15, TimeUnit.SECONDS));
            assertTrue(isLockAcquisitionFailure(failure.getCause()),
                "Expected a lock-acquisition failure, but was: " + failure.getCause());

            releaseLock.countDown();
            blocker.get(15, TimeUnit.SECONDS);
        } finally {
            releaseLock.countDown();
            pool.shutdownNow();
        }

        assertEquals(10, postService.getLikes(id)); // only A committed
    }

    private static void awaitCondition(BooleanSupplier condition, long timeout, TimeUnit unit)
            throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        while (!condition.getAsBoolean()) {
            if (System.nanoTime() > deadline) {
                fail("Condition was not met within the timeout");
            }
            Thread.sleep(25);
        }
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
