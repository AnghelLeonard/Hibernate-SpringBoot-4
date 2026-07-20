package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.repository.PostJpaRepository;
import com.bookstore.forum.repository.PostRepository;
import io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    // tag::standard-save[]
    @Test
    public void standardSaveOnDetachedEntityTriggersSelectThenUpdate() {
        Long id = persistPost("Original title");

        transactionTemplate.executeWithoutResult(status -> {
            Post detached = new Post("Changed via standard save");
            detached.setId(id);

            SQLStatementCountValidator.reset();
            postJpaRepository.save(detached);
            entityManager.flush();

            // save() is a merge: it reads the row before updating it.
            SQLStatementCountValidator.assertSelectCount(1);
            SQLStatementCountValidator.assertUpdateCount(1);
        });
    }
    // end::standard-save[]

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
    public void updateAllIssuesADirectUpdatePerEntityWithoutAnySelect() {
        List<Long> ids = persistPosts(3);

        transactionTemplate.executeWithoutResult(status -> {
            List<Post> detached = detachedCopies(ids, "Changed via updateAll");

            SQLStatementCountValidator.reset();
            postRepository.updateAllAndFlush(detached);

            // updateAll() delegates to StatelessSession.update per row: a direct
            // UPDATE with NO SELECT. Note it does NOT batch here — a single
            // StatelessSession.update forces the JDBC batch size to 0 (only the
            // *Multiple stateless operations batch), so three rows leave as three
            // UPDATE statements. The win over saveAll below is the absence of the
            // per-entity SELECTs, not fewer UPDATEs.
            SQLStatementCountValidator.assertSelectCount(0);
            SQLStatementCountValidator.assertUpdateCount(3);
        });
    }

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

    @Test
    public void lockByIdAcquiresPessimisticWriteLock() {
        Long id = persistPost("Original title");

        transactionTemplate.executeWithoutResult(status -> {
            Post locked = postRepository.lockById(id, LockModeType.PESSIMISTIC_WRITE);

            assertEquals(LockModeType.PESSIMISTIC_WRITE, entityManager.getLockMode(locked));
        });
    }
}
