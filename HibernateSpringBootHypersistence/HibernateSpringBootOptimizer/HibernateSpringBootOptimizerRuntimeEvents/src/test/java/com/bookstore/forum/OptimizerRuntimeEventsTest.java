package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.hibernate.event.query.PaginationWithoutOrderByEvent;
import io.hypersistence.optimizer.hibernate.event.session.NPlusOneQueryEntityFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.session.SecondaryQueryEntityFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.session.SessionTimeoutEvent;
import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizerRuntimeEventsTest.class);

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
    public void paginationWithoutAnOrderByIsReported() {
        forumService.findPage(0, 2);
        logEvents();
        assertEventTriggered(1, PaginationWithoutOrderByEvent.class);

        hypersistenceOptimizer.getEvents().clear();
        forumService.findPageOrdered(0, 2);
        assertEventTriggered(0, PaginationWithoutOrderByEvent.class);
    }

    @Test
    public void aSessionThatOutstaysItsWelcomeIsReported() {
        forumService.slowTransaction(200);
        logEvents();
        assertEventTriggered(1, SessionTimeoutEvent.class);
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
        logEvents();
        assertEventTriggered(5, SecondaryQueryEntityFetchingEvent.class);
        assertEventTriggered(1, NPlusOneQueryEntityFetchingEvent.class);

        hypersistenceOptimizer.getEvents().clear();
        forumService.findCommentAuthorsInOneQuery();
        logEvents();
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

    private void logEvents() {
        for (Event event : hypersistenceOptimizer.getEvents()) {
            LOGGER.info("[{}] {} — {}",
                event.getPriority(), event.getClass().getName(), event.getDescription());
        }
    }
}
