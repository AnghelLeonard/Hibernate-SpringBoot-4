package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.entity.Post;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.hibernate.event.mapping.EntityAttributeMappingEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.basic.EnumTypeStringEvent;
import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;
import java.util.stream.Collectors;

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
     * A handful of lines, no setup, and it guards every mapping in the module.
     * The message building is factored out so the test itself stays a one-liner.
     */
    // tag::ci-test[]
    @Test
    public void theMappingsAreClean() {
        List<Event> events = listEventHandler.getEvents();

        assertTrue(events.isEmpty(), () -> unacceptedIssuesMessage(events));
    }
    // end::ci-test[]

    private static String unacceptedIssuesMessage(List<Event> events) {
        String issues = events.stream()
            .map(OptimizerEventFiltersTest::formatEvent)
            .collect(Collectors.joining("\n"));

        return String.format(
            "Hypersistence Optimizer reported issues that nobody has accepted:%n%s",
            issues
        );
    }

    private static String formatEvent(Event event) {
        return String.format("  [%s] %s — %s",
            event.getPriority(), event.getClass().getSimpleName(), event.getDescription());
    }

    /**
     * A filter that suppresses nothing is a filter you will forget to remove.
     * Scanning the same {@code EntityManagerFactory} <em>without</em> the filter
     * proves the accepted trade-off is real, and that exactly one issue — the
     * String-mapped {@code Post.status} enum — is being waived, not a category
     * of them.
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
        assertTrue(waived instanceof EnumTypeStringEvent, "Expected an EnumTypeStringEvent");
        assertEquals(Post.class, ((EntityAttributeMappingEvent) waived).getEntityClass());
        assertEquals("status", ((EntityAttributeMappingEvent) waived).getEntityAttribute());
    }
    // end::waiver-test[]
}
