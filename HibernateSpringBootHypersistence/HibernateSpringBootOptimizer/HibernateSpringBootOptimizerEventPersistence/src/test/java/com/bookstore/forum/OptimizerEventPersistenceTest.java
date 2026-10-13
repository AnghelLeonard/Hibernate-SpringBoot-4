package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Proves the Event Persistence feature end to end: the Optimizer creates the
 * three {@code ho_*} tables in the same database, records this startup as a run,
 * rolls up the runtime events it detects, and serves them from the embedded
 * web app — all without a second {@code DataSource}.
 */
@SpringBootTest
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@ActiveProfiles("test")
class OptimizerEventPersistenceTest {

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        AbstractContainerDataSourceProvider provider = DatabaseType.POSTGRESQL.provider();
        registry.add("spring.datasource.url", provider::url);
        registry.add("spring.datasource.username", provider::username);
        registry.add("spring.datasource.password", provider::password);
    }

    @Autowired
    private HypersistenceOptimizer hypersistenceOptimizer;

    @Autowired
    private ForumService forumService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        forumService.deleteAll();
        for (int i = 1; i <= 5; i++) {
            forumService.createPost("Post " + i, List.of("Great read", "Very useful"));
        }
    }

    // tag::persisted-events-test[]
    @Test
    public void theDetectedEventsArePersisted() {
        // The startup itself is recorded as a run, whatever the mapping looks like.
        assertTrue(rowCount("ho_bootstrap_run") >= 1,
            "The application startup was not recorded as a run");

        // Two scenarios that each produce a runtime event: a result list larger
        // than the configured limit, and an N+1 query walking a lazy association.
        forumService.findComments();
        forumService.findCommentAuthorsOneByOne();

        // Force the pending rollups to the database instead of waiting for the
        // rollup window to elapse, so the assertion is deterministic.
        hypersistenceOptimizer.getEventPersistenceContext().flushRuntimeEvents();

        assertTrue(rowCount("ho_runtime_events") >= 1,
            "The runtime events were not rolled up into ho_runtime_events");
    }
    // end::persisted-events-test[]

    @Test
    public void theWebAppServesTheStoredEvents() throws Exception {
        String webAppUrl = hypersistenceOptimizer.getWebAppUrl();
        assertNotNull(webAppUrl, "The embedded web app did not start");

        HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
        HttpResponse<String> response = httpClient.send(
            HttpRequest.newBuilder(URI.create(webAppUrl))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "The web app did not serve its page");
    }

    private long rowCount(String table) {
        return jdbcTemplate.queryForObject("select count(*) from " + table, Long.class);
    }
}
