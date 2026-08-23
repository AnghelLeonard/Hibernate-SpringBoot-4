package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.hibernate.event.mapping.identifier.IdentityGeneratorEvent;
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
 * The MySQL counterpart of {@code OptimizerMappingFixesTest}. The same forum
 * model cannot reach zero events here, because MySQL has no sequences and so
 * forces {@code IDENTITY}, which the Optimizer reports for every entity that
 * owns an identifier. That one database-wide constraint is waived with an
 * {@code EventFilter}, and the build still fails on anything else.
 */
@SpringBootTest
@EnabledIfDatabaseAvailable(DatabaseType.MYSQL)
@ActiveProfiles("test")
class OptimizerMySQLIdentityTest {

    /**
     * The application's own Hikari pool is used, so the connection-related
     * settings that clear the JDBC events are the real ones.
     */
    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        AbstractContainerDataSourceProvider provider = DatabaseType.MYSQL.provider();
        registry.add("spring.datasource.url",
            () -> provider.url() + (provider.url().contains("?") ? "&" : "?") + "rewriteBatchedStatements=true");
        registry.add("spring.datasource.username", provider::username);
        registry.add("spring.datasource.password", provider::password);
    }

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Keeps the waiver honest: scanning the same {@code EntityManagerFactory}
     * without the filter proves that everything left is an
     * {@code IdentityGeneratorEvent}, so the filter hides the database
     * constraint and nothing else.
     */
    // tag::waiver-test[]
    @Test
    public void everythingWaivedIsAnIdentityEvent() {
        List<Event> unfiltered = new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory)).getEvents();

        assertEquals(3, unfiltered.size());
        assertTrue(
            unfiltered.stream().allMatch(IdentityGeneratorEvent.class::isInstance),
            () -> "Only identity events expected, got: " + unfiltered.stream()
                .map(event -> event.getClass().getSimpleName())
                .toList());
    }
    // end::waiver-test[]

    /**
     * The gating test: with the IDENTITY events waived, the scan is clean, so
     * any other regression still fails the build.
     */
    // tag::clean-scan[]
    @Test
    public void reportsOnlyTheWaivedIdentityIssues() {
        List<Event> events = hypersistenceOptimizer.getEvents();

        assertTrue(events.isEmpty(),
            () -> "Expected a clean scan, but got: " + events.stream()
                .map(event -> event.getClass().getSimpleName())
                .toList());
    }
    // end::clean-scan[]
}
