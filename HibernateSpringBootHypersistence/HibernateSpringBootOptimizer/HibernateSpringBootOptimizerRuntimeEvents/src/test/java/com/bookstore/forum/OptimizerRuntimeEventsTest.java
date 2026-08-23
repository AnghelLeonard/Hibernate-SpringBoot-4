package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.hibernate.event.query.PaginationWithoutOrderByEvent;
import io.hypersistence.optimizer.hibernate.event.query.QueryResultListSizeEvent;
import io.hypersistence.optimizer.hibernate.event.query.QueryTimeoutEvent;
import io.hypersistence.optimizer.hibernate.event.session.EntityAlreadyManagedEvent;
import io.hypersistence.optimizer.hibernate.event.session.NPlusOneQueryEntityFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.session.SecondaryQueryEntityFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.session.SessionFlushTimeoutEvent;
import io.hypersistence.optimizer.hibernate.event.session.SessionTimeoutEvent;
import io.hypersistence.optimizer.hibernate.event.session.TableRowAlreadyManagedEvent;
import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The mapping is the fixed one from the previous item, so the startup scan is
 * silent. Everything reported here comes from the <em>runtime</em> scanner —
 * problems that live in application code, not in annotations, and that no amount
 * of mapping review would ever surface.
 */
@SpringBootTest
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@ActiveProfiles("test")
class OptimizerRuntimeEventsTest {

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

    @BeforeEach
    public void setUp() {
        forumService.deleteAll();
        forumService.createTag("hibernate");
        for (int i = 1; i <= 5; i++) {
            forumService.createPost(
                "Post " + i, "Vlad Mihalcea", List.of("hibernate"),
                List.of("Great read", "Very useful"));
        }
        // Everything above is setup noise; the scenarios below start from zero.
        hypersistenceOptimizer.getEvents().clear();
    }

    @Test
    public void aLargeResultListIsReported() {
        // tag::result-list-test[]
        forumService.findComments();
        assertEventTriggered(1, QueryResultListSizeEvent.class);
        QueryResultListSizeEvent event =
            triggeredEvent(QueryResultListSizeEvent.class);
        assertEquals(10, event.getResultListSize(), "Unexpected result list size");
        // end::result-list-test[]
    }

    @Test
    public void aSlowQueryIsReported() {
        // tag::slow-query-test[]
        forumService.runSlowQuery();
        assertEventTriggered(1, QueryTimeoutEvent.class);
        QueryTimeoutEvent event = triggeredEvent(QueryTimeoutEvent.class);
        assertTrue(event.getQueryTimeMillis() >= 100, "Query time under the limit");
        // end::slow-query-test[]
    }

    @Test
    public void aSessionThatOutstaysItsWelcomeIsReported() {
        // tag::session-timeout-test[]
        forumService.slowTransaction(200);
        assertEventTriggered(1, SessionTimeoutEvent.class);
        SessionTimeoutEvent event = triggeredEvent(SessionTimeoutEvent.class);
        assertTrue(event.getSessionTimeMillis() >= 100, "Session time under the limit");
        // end::session-timeout-test[]
    }

    @Test
    public void aSlowFlushIsReported() {
        // tag::slow-flush-test[]
        forumService.flushManyEntities();
        assertEventTriggered(1, SessionFlushTimeoutEvent.class);
        SessionFlushTimeoutEvent event =
            triggeredEvent(SessionFlushTimeoutEvent.class);
        assertTrue(event.getFlushTimeMillis() >= 100, "Flush time under the limit");
        // end::slow-flush-test[]
    }

    @Test
    public void paginationWithoutAnOrderByIsReported() {
        // tag::pagination-test[]
        forumService.findPage(0, 2);
        assertEventTriggered(1, PaginationWithoutOrderByEvent.class);

        hypersistenceOptimizer.getEvents().clear();
        forumService.findPageOrdered(0, 2);
        assertEventTriggered(0, PaginationWithoutOrderByEvent.class);
        // end::pagination-test[]
    }

    @Test
    public void mergingAnAlreadyManagedEntityIsReported() {
        Long postId = forumService.findPageOrdered(0, 1).get(0).getId();
        hypersistenceOptimizer.getEvents().clear();

        // tag::already-managed-test[]
        forumService.mergeAnAlreadyManagedEntity(postId);
        assertEventTriggered(1, EntityAlreadyManagedEvent.class);
        EntityAlreadyManagedEvent event =
            triggeredEvent(EntityAlreadyManagedEvent.class);
        assertEquals("merge", event.getEntityStateTransitionMethodName(),
            "Unexpected state transition method");
        // end::already-managed-test[]
    }

    @Test
    public void loadingOneRowAsTwoEntitiesIsReported() {
        Long postId = forumService.findPageOrdered(0, 1).get(0).getId();
        hypersistenceOptimizer.getEvents().clear();

        // tag::table-row-test[]
        forumService.loadSameRowAsTwoEntities(postId);
        assertEventTriggered(1, TableRowAlreadyManagedEvent.class);
        // end::table-row-test[]
    }

    /**
     * Five posts, ten comments, one loop. The Optimizer reports it twice over:
     * once per lazily resolved parent ({@code SecondaryQueryEntityFetchingEvent}
     * — "this row was loaded by a query of its own"), and once as the summary
     * ({@code NPlusOneQueryEntityFetchingEvent} — "Post was fetched 5 times in
     * this Session"). Replacing the loop with a {@code join fetch} silences both.
     */
    // tag::n-plus-one-test[]
    @Test
    public void theNPlusOneQueryHidingInApplicationCodeIsReported() {
        forumService.findCommentAuthorsOneByOne();
        assertEventTriggered(5, SecondaryQueryEntityFetchingEvent.class);
        assertEventTriggered(1, NPlusOneQueryEntityFetchingEvent.class);

        hypersistenceOptimizer.getEvents().clear();
        forumService.findCommentAuthorsInOneQuery();
        assertEventTriggered(0, SecondaryQueryEntityFetchingEvent.class);
        assertEventTriggered(0, NPlusOneQueryEntityFetchingEvent.class);
    }
    // end::n-plus-one-test[]

    private void assertEventTriggered(int expectedCount, Class<? extends Event> eventClass) {
        long count = hypersistenceOptimizer.getEvents().stream()
            .filter(event -> event.getClass().equals(eventClass))
            .count();

        assertEquals(expectedCount, count, () -> "Unexpected number of " + eventClass.getSimpleName());
    }

    private <T extends Event> T triggeredEvent(Class<T> eventClass) {
        return hypersistenceOptimizer.getEvents().stream()
            .filter(event -> event.getClass().equals(eventClass))
            .map(eventClass::cast)
            .findFirst()
            .orElseThrow(() -> new AssertionError("No event of type " + eventClass.getSimpleName() + " was triggered"));
    }
}
