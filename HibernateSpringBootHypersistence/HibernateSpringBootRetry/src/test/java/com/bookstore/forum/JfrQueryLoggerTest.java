package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.service.PostService;
import com.bookstore.forum.service.RetryableForumService;
import io.hypersistence.utils.hibernate.query.JfrQueryLogger;
import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordingFile;
import org.hibernate.cfg.AvailableSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Shows that {@link JfrQueryLogger} emits a JDK Flight Recorder event for every
 * SQL statement Hibernate runs, so the queries can be captured from a recording
 * the same way any other JFR event is (GC pauses, virtual-thread pinning, and so
 * on). The retry example fires a {@code SELECT ... FOR UPDATE}, which turns up in
 * the recording as a {@code JfrQueryLogger} query event.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import({TestDataSourceConfiguration.class,
    JfrQueryLoggerTest.StatementInspectorConfiguration.class})
@ActiveProfiles("test")
class JfrQueryLoggerTest {

    private static final String QUERY_EVENT =
        "io.hypersistence.utils.hibernate.query.JfrQueryLogger$QueryEvent";

    @Autowired
    private PostService postService;

    @Autowired
    private RetryableForumService retryableForumService;

    @BeforeEach
    public void cleanUp() {
        postService.deleteAll();
    }

    // tag::jfr-test[]
    @Test
    public void jfrQueryLoggerRecordsEachStatementAsAJfrEvent() throws Exception {
        Long id = postService.createPost("Recorded post");

        List<String> recordedSql = recordQueries(() ->
            retryableForumService.incrementLikes(id, 1, new AtomicInteger()));

        // The SELECT ... FOR UPDATE that PostService.incrementLikes fires shows
        // up in the recording, right next to the JVM's own GC and thread events.
        assertTrue(recordedSql.stream()
            .anyMatch(sql -> sql.contains("for no key update")));
    }

    private List<String> recordQueries(ThrowingAction action) throws Exception {
        Path jfrFile = Files.createTempFile("query", ".jfr");
        try (Recording recording = new Recording()) {
            recording.enable(QUERY_EVENT);
            recording.start();
            action.run();
            recording.stop();
            recording.dump(jfrFile);
        }
        try {
            return RecordingFile.readAllEvents(jfrFile).stream()
                .filter(event -> event.getEventType().getName().equals(QUERY_EVENT))
                .map(event -> event.getString("sql"))
                .toList();
        } finally {
            Files.deleteIfExists(jfrFile);
        }
    }

    private interface ThrowingAction {
        void run() throws Exception;
    }
    // end::jfr-test[]

    // tag::jfr-inspector[]
    @TestConfiguration
    static class StatementInspectorConfiguration {

        @Bean
        public HibernatePropertiesCustomizer jfrQueryLogger() {
            return properties -> properties.put(
                AvailableSettings.STATEMENT_INSPECTOR,
                new JfrQueryLogger());
        }
    }
    // end::jfr-inspector[]
}
