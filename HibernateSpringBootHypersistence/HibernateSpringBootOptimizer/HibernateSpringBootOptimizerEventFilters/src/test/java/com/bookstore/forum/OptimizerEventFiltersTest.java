package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.entity.Post;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.hibernate.event.mapping.EntityAttributeMappingEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import jakarta.persistence.EntityManagerFactory;
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
 * The chapter's closing argument. This is the test you actually commit: it does
 * not measure anything, it does not need a fixture, and it fails the build the
 * day somebody adds an eager association, drops an {@code @MapsId}, or turns
 * {@code ddl-auto} back on.
 */
@SpringBootTest
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@ActiveProfiles("test")
class OptimizerEventFiltersTest {

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        AbstractContainerDataSourceProvider provider = DatabaseType.POSTGRESQL.provider();
        registry.add("spring.datasource.url",
            () -> provider.url() + (provider.url().contains("?") ? "&" : "?") + "reWriteBatchedInserts=true");
        registry.add("spring.datasource.username", provider::username);
        registry.add("spring.datasource.password", provider::password);
    }

    @Autowired
    private ListEventHandler listEventHandler;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Nine lines, no setup, and it guards every mapping in the module.
     */
    // tag::ci-test[]
    @Test
    public void theMappingsAreClean() {
        List<Event> events = listEventHandler.getEvents();

        assertTrue(events.isEmpty(),
            () -> "Hypersistence Optimizer reported issues that nobody has accepted:\n" + events.stream()
                .map(event -> "  [" + event.getPriority() + "] "
                    + event.getClass().getSimpleName() + " — " + event.getDescription())
                .reduce("", (a, b) -> a + b + "\n"));
    }
    // end::ci-test[]

    /**
     * A filter that suppresses nothing is a filter you will forget to remove.
     * Scanning the same {@code EntityManagerFactory} <em>without</em> the filter
     * proves the accepted trade-off is real, and that exactly one issue is being
     * waived — not a category of them.
     */
    // tag::waiver-test[]
    @Test
    public void exactlyOneIssueIsBeingWaived() {
        List<Event> unfiltered =
            new HypersistenceOptimizer(new JpaConfig(entityManagerFactory)).getEvents();

        assertEquals(1, unfiltered.size(),
            () -> "Expected the single accepted trade-off, but got: " + unfiltered.stream()
                .map(event -> event.getClass().getSimpleName()).toList());

        Event waived = unfiltered.get(0);
        assertTrue(waived instanceof EagerFetchingEvent, "Expected an EagerFetchingEvent");
        assertEquals(Post.class, ((EntityAttributeMappingEvent) waived).getEntityClass());
        assertEquals("status", ((EntityAttributeMappingEvent) waived).getEntityAttribute());
    }
    // end::waiver-test[]
}
