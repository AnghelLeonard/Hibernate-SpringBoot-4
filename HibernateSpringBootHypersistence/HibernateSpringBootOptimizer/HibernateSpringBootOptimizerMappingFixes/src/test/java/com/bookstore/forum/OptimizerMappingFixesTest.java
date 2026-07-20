package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The closing argument of the previous two items: the same forum model, scanned
 * by the same single bean, now reports <em>nothing</em>.
 *
 * <p>Note that this test uses the application's own Hikari {@code DataSource}
 * rather than the shared test one. That is deliberate: two of the fourteen
 * issues ({@code SkipAutoCommitCheckEvent}, {@code JdbcBatchToBulkInsertEvent})
 * are properties of the connection pool and the JDBC URL, so they can only be
 * honestly fixed against the real thing.</p>
 */
@SpringBootTest
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@ActiveProfiles("test")
class OptimizerMappingFixesTest {

    /**
     * Only the coordinates come from the shared provider (which resolves the
     * local server and its credentials, or falls back to a container) — the
     * {@code DataSource} itself is still the application's Hikari pool, so
     * {@code auto-commit=false} and {@code reWriteBatchedInserts=true} are real.
     */
    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        AbstractContainerDataSourceProvider provider = DatabaseType.POSTGRESQL.provider();
        registry.add("spring.datasource.url",
            () -> provider.url() + (provider.url().contains("?") ? "&" : "?") + "reWriteBatchedInserts=true");
        registry.add("spring.datasource.username", provider::username);
        registry.add("spring.datasource.password", provider::password);
    }

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Autowired
    private ForumService forumService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    public void setUp() {
        forumService.deleteAll();
    }

    /**
     * The money shot: no events at all. Drop this assertion into your build and
     * any future mapping regression fails it.
     */
    // tag::clean-scan[]
    @Test
    public void reportsNoIssuesAtAll() {
        List<Event> events = hypersistenceOptimizer.getEvents();

        assertTrue(events.isEmpty(),
            () -> "Expected a clean scan, but got: " + events.stream()
                .map(event -> event.getClass().getSimpleName() + " — " + event.getDescription())
                .toList());
    }
    // end::clean-scan[]

    /**
     * A clean scan is worth nothing if the model stopped working, so the fixed
     * mappings are exercised too — including the {@code @MapsId} identifier the
     * details now share with their post.
     */
    @Test
    public void theFixedModelStillWorks() {
        forumService.createTag("hibernate");
        forumService.createTag("jpa");

        Post post = forumService.createPost(
            "High-Performance Java Persistence", "Vlad Mihalcea",
            List.of("hibernate", "jpa"),
            List.of("Great read", "Very useful"));

        // Everything is lazy now, so the associations have to be walked inside a
        // transaction — which is exactly the discipline the fixed mapping buys.
        transactionTemplate.executeWithoutResult(status -> {
            Post loaded = forumService.findById(post.getId());
            assertEquals(post.getId(), loaded.getDetails().getId(),
                "@MapsId means the details share the post identifier");
            assertEquals(2, loaded.getComments().size());
            assertEquals(2, loaded.getTags().size());
        });

        forumService.removeTag(post.getId(), "jpa");

        transactionTemplate.executeWithoutResult(status ->
            assertEquals(1, forumService.findById(post.getId()).getTags().size()));
    }

    /**
     * The association is genuinely lazy now — the comment arrives without its
     * post, which is what stops the N+1 the "before" model guaranteed.
     */
    @Test
    public void theManyToOneIsLazy() {
        forumService.createPost("Lazy by default", "Vlad Mihalcea", List.of(), List.of("Nice"));

        List<PostComment> comments = forumService.findComments();

        assertEquals(1, comments.size());
        assertFalse(Hibernate.isInitialized(comments.get(0).getPost()),
            "The parent post must still be an uninitialized proxy");
    }
}
