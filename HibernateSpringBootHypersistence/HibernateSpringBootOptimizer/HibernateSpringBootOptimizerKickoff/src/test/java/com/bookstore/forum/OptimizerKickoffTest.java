package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.hibernate.event.configuration.batching.JdbcBatchSizeEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.SkipAutoCommitCheckEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.logging.ConsoleStatementLoggingEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.query.DefaultQueryPlanCacheMaxSizeEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.query.QueryInClauseParameterPaddingEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.query.QueryPaginationCollectionFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.schema.SchemaGenerationEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.BidirectionalSynchronizationEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.ManyToManyListEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneParentSideEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneWithoutMapsIdEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.optimizer.jdbc.event.batching.JdbcBatchToBulkInsertEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pins every issue Hypersistence Optimizer finds in the deliberately suboptimal
 * "before" model. The point of the item is that this is a <em>test</em>, not a
 * log to skim: the scan result is a {@code List<Event>}, so a mapping mistake
 * can fail a build. The next item removes these one at a time.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class OptimizerKickoffTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizerKickoffTest.class);

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    /**
     * What the mappings cost. Each of these is a design decision that no runtime
     * tuning can undo, because the fetch plan and the table layout are fixed at
     * boot time.
     */
    // tag::mapping-events[]
    @Test
    public void reportsTheMappingIssues() {
        logEvents();

        // PostComment.post and PostDetails.post are both EAGER by default.
        assertEventTriggered(2, EagerFetchingEvent.class);
        // Post.details is the mappedBy side, so Hibernate cannot proxy it.
        assertEventTriggered(1, OneToOneParentSideEvent.class);
        // PostDetails has a surrogate key instead of sharing the post's.
        assertEventTriggered(1, OneToOneWithoutMapsIdEvent.class);
        // Removing one tag rewrites the whole join table for that post.
        assertEventTriggered(1, ManyToManyListEvent.class);
        // Post has addComment(...) but no matching remove method.
        assertEventTriggered(1, BidirectionalSynchronizationEvent.class);
    }
    // end::mapping-events[]

    /**
     * What the configuration costs. These have nothing to do with the entities —
     * the Optimizer reads the Hibernate settings behind the
     * {@code EntityManagerFactory} too.
     */
    @Test
    public void reportsTheConfigurationIssues() {
        // ddl-auto=create: the schema is generated rather than migrated.
        assertEventTriggered(1, SchemaGenerationEvent.class);
        // No hibernate.jdbc.batch_size, so every insert is its own roundtrip...
        assertEventTriggered(1, JdbcBatchSizeEvent.class);
        // ...and on PostgreSQL, reWriteBatchedInserts is off as well.
        assertEventTriggered(1, JdbcBatchToBulkInsertEvent.class);
        // show-sql prints through System.out instead of a logger.
        assertEventTriggered(1, ConsoleStatementLoggingEvent.class);
        // Hibernate cannot know the pool already disabled autocommit.
        assertEventTriggered(1, SkipAutoCommitCheckEvent.class);
        // Paginating a join-fetched collection would paginate in memory.
        assertEventTriggered(1, QueryPaginationCollectionFetchingEvent.class);
        // Varying IN-clause sizes compile a new plan each time.
        assertEventTriggered(1, QueryInClauseParameterPaddingEvent.class);
        // The default 2048-entry query plan cache is rarely enough.
        assertEventTriggered(1, DefaultQueryPlanCacheMaxSizeEvent.class);
    }

    /**
     * Fourteen issues in a model of four entities and a nine-line properties
     * file — and not one line of application code had to run to find them.
     */
    @Test
    public void reportsNothingElse() {
        assertEquals(14, hypersistenceOptimizer.getEvents().size(),
            () -> "Unexpected event list: " + hypersistenceOptimizer.getEvents().stream()
                .map(event -> event.getClass().getSimpleName()).toList());
    }

    private void assertEventTriggered(int expectedCount, Class<? extends Event> eventClass) {
        long count = hypersistenceOptimizer.getEvents().stream()
            .filter(event -> event.getClass().equals(eventClass))
            .count();

        assertEquals(expectedCount, count, () -> "Unexpected number of " + eventClass.getSimpleName());
    }

    private void logEvents() {
        for (Event event : hypersistenceOptimizer.getEvents()) {
            LOGGER.info("[{}] {} — {}",
                event.getPriority(), event.getClass().getSimpleName(), event.getDescription());
        }
    }
}
