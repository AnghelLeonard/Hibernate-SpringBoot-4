package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostStatus;
import com.bookstore.forum.repository.PostJpaRepository;
import com.bookstore.forum.repository.PostRepository;
import com.bookstore.forum.repository.PostSummary;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The proof that {@code BaseJpaRepository} beats the standard {@code JpaRepository}
 * is the executed SQL, so each test resets {@link SQLStatementCountValidator}
 * (fed by the datasource-proxy in {@link TestDataSourceConfiguration}) and pins
 * the exact statement counts.
 *
 * <ul>
 *   <li>standard {@code save(detached)} &rarr; SELECT + UPDATE (a wasted read),</li>
 *   <li>{@code update(detached)} &rarr; UPDATE only (StatelessSession, no SELECT),</li>
 *   <li>{@code persistAllAndFlush(newPosts)} &rarr; a single batched INSERT,</li>
 *   <li>{@code lockById} &rarr; a pessimistic write lock.</li>
 * </ul>
 */
@SpringBootTest(properties = "test.database=MYSQL")
@EnabledIfDatabaseAvailable(DatabaseType.MYSQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class BaseJpaRepositoryTest {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    @Qualifier("forumService")
    private ForumService forumService;

    @Autowired
    @Qualifier("antiPatternForumService")
    private ForumService antiPatternForumService;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void cleanUp() {
        transactionTemplate.executeWithoutResult(status -> postJpaRepository.deleteAllInBatch());
    }

    private Long persistPost(String title) {
        return transactionTemplate.execute(status -> postRepository.persist(new Post(title)).getId());
    }

    private List<Long> persistPosts(int count) {
        return transactionTemplate.execute(status -> {
            List<Post> posts = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                posts.add(new Post("Original title " + i));
            }
            return postRepository.persistAll(posts).stream().map(Post::getId).toList();
        });
    }

    private static List<Post> detachedCopies(List<Long> ids, String titlePrefix) {
        List<Post> detached = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            Post post = new Post(titlePrefix + " " + i);
            post.setId(ids.get(i));
            detached.add(post);
        }
        return detached;
    }

    @Test
    public void standardSaveOnDetachedEntityTriggersSelectThenUpdate() {
        // tag::standard-save[]
        Long id = persistPost("Original title");

        transactionTemplate.executeWithoutResult(status -> {
            Post detached = new Post("Changed via standard save");
            detached.setId(id);

            SQLStatementCountValidator.reset();
            postJpaRepository.save(detached);
            entityManager.flush();

            // save() calls merge, so it reads the row before updating it.
            SQLStatementCountValidator.assertSelectCount(1);
            SQLStatementCountValidator.assertUpdateCount(1);
        });
        // end::standard-save[]
    }

    // tag::base-update[]
    @Test
    public void baseUpdateOnDetachedEntityTriggersUpdateWithoutSelect() {
        Long id = persistPost("Original title");

        transactionTemplate.executeWithoutResult(status -> {
            Post detached = new Post("Changed via update");
            detached.setId(id);

            SQLStatementCountValidator.reset();
            postRepository.update(detached);

            // update() delegates to StatelessSession.update: a direct UPDATE.
            SQLStatementCountValidator.assertSelectCount(0);
            SQLStatementCountValidator.assertUpdateCount(1);
        });
    }
    // end::base-update[]

    @Test
    public void persistAllIssuesASingleBatchedInsert() {
        transactionTemplate.executeWithoutResult(status -> {
            SQLStatementCountValidator.reset();
            postRepository.persistAllAndFlush(List.of(
                new Post("Post A"), new Post("Post B"), new Post("Post C")));

            // All three rows go out in one JDBC batch.
            SQLStatementCountValidator.assertInsertCount(1);
        });
    }

    @Test
    public void updateAllAndFlushBatchesTheUpdatesWithoutAnySelect() {
        List<Long> ids = persistPosts(3);

        transactionTemplate.executeWithoutResult(status -> {
            List<Post> detached = detachedCopies(ids, "Changed via updateAll");

            SQLStatementCountValidator.reset();
            postRepository.updateAllAndFlush(detached);

            // updateAllAndFlush() delegates to StatelessSession.update per row: a
            // direct UPDATE with NO SELECT. As of Hypersistence Utils 3.15.5 the
            // three rows are also sent as a SINGLE batched UPDATE (see
            // updateAllBatching). The win over saveAll below is the absence of the
            // per-entity SELECTs.
            SQLStatementCountValidator.assertSelectCount(0);
            SQLStatementCountValidator.assertUpdateCount(1);
        });
    }

    // tag::updateall-batch[]
    @Test
    public void updateAllBatching() {
        // JDBC batching is enabled (hibernate.jdbc.batch_size=100), and as of
        // Hypersistence Utils 3.15.5 updateAll batches the StatelessSession UPDATEs
        // too: the configured batch size now reaches the StatelessSession that
        // actually writes the rows.
        int entityCount = 5;
        List<Long> ids = persistPosts(entityCount);

        transactionTemplate.executeWithoutResult(status -> {
            List<Post> detached = detachedCopies(ids, "Changed via updateAll");

            SQLStatementCountValidator.reset();
            postRepository.updateAll(detached);

            // No wasted SELECTs (StatelessSession writes directly), and the 5 rows
            // leave in a SINGLE batched UPDATE instead of one statement per row.
            SQLStatementCountValidator.assertSelectCount(0);
            SQLStatementCountValidator.assertUpdateCount(1);
        });
    }
    // end::updateall-batch[]

    @Test
    public void standardSaveAllIssuesASelectPerDetachedEntity() {
        List<Long> ids = persistPosts(3);

        transactionTemplate.executeWithoutResult(status -> {
            List<Post> detached = detachedCopies(ids, "Changed via standard saveAll");

            SQLStatementCountValidator.reset();
            postJpaRepository.saveAll(detached);
            entityManager.flush();

            // saveAll() merges each detached entity, so it reads every row back
            // first: three SELECTs that updateAll() avoids entirely. The dirty
            // managed copies are then flushed as one batched UPDATE.
            SQLStatementCountValidator.assertSelectCount(3);
            SQLStatementCountValidator.assertUpdateCount(1);
        });
    }

    /**
     * Seeds 50 posts. The 30 APPROVED ones carry the low view counts (1..30),
     * while the 20 non-approved ones carry the <em>high</em> counts (100..119).
     * That ordering is deliberate: a "top by views" that forgets the status
     * filter would return the spam, so the filter genuinely changes the answer.
     */
    private void seedForFindAll() {
        transactionTemplate.executeWithoutResult(status -> {
            List<Post> posts = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                posts.add(new Post("Approved #" + i, PostStatus.APPROVED, i));
            }
            for (int i = 0; i < 20; i++) {
                PostStatus s = (i % 2 == 0) ? PostStatus.PENDING : PostStatus.SPAM;
                posts.add(new Post("Noise #" + i, s, 100 + i));
            }
            postRepository.persistAll(posts);
        });
    }

    // tag::findall-antipattern[]
    @Test
    public void findAllThenFilterInMemoryIsAnAntiPattern() {
        seedForFindAll(); // 50 posts, only 30 of them APPROVED

        // Same method, same signature, called on both services. The broken one
        // (antiPatternForumService) fetches everything with findAll() and filters
        // in memory; the good one (forumService) filters in SQL.
        SQLStatementCountValidator.reset();
        List<PostSummary> inMemory = antiPatternForumService.findMostViewedAndApprovedPosts(5);
        SQLStatementCountValidator.assertSelectCount(1);

        SQLStatementCountValidator.reset();
        List<PostSummary> onDatabase = forumService.findMostViewedAndApprovedPosts(5);
        SQLStatementCountValidator.assertSelectCount(1);

        // Same answer: the top 5 APPROVED posts are the highest-numbered ones.
        List<PostSummary> expected = List.of(
            new PostSummary("Approved #30", 30), new PostSummary("Approved #29", 29),
            new PostSummary("Approved #28", 28), new PostSummary("Approved #27", 27),
            new PostSummary("Approved #26", 26));
        assertEquals(expected, inMemory);
        assertEquals(expected, onDatabase);
        assertEquals(inMemory, onDatabase);

        // Both report a single SELECT, so the anti-pattern is invisible from here:
        // only the AntiPatternForumService source and the executed SQL reveal that
        // it read all 50 rows to keep 5.
    }
    // end::findall-antipattern[]

    @Test
    public void lockByIdAcquiresPessimisticWriteLock() {
        Long id = persistPost("Original title");

        transactionTemplate.executeWithoutResult(status -> {
            Post locked = postRepository.lockById(id, LockModeType.PESSIMISTIC_WRITE);

            assertEquals(LockModeType.PESSIMISTIC_WRITE, entityManager.getLockMode(locked));
        });
    }
}
