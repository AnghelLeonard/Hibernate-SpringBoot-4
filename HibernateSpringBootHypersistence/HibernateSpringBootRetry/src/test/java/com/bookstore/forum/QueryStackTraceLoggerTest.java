package com.bookstore.forum;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.service.PostService;
import com.bookstore.forum.service.RetryableForumService;
import io.hypersistence.utils.hibernate.query.QueryStackTraceLogger;
import org.hibernate.cfg.AvailableSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Shows that {@link QueryStackTraceLogger} names the application method that
 * fired a query, even when the call reaches Hibernate through the {@code @Retry}
 * aspect and two service beans. The test calls
 * {@link RetryableForumService#incrementLikes}, which delegates to
 * {@link PostService#incrementLikes}, which issues the {@code SELECT ... FOR
 * UPDATE}. The logged stack trace points straight at {@code PostService}, not at
 * the proxy or the Hibernate internals in between.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import({TestDataSourceConfiguration.class,
    QueryStackTraceLoggerTest.StatementInspectorConfiguration.class})
@ActiveProfiles("test")
class QueryStackTraceLoggerTest {

    @Autowired
    private PostService postService;

    @Autowired
    private RetryableForumService retryableForumService;

    @BeforeEach
    public void cleanUp() {
        postService.deleteAll();
    }

    // tag::stacktrace-test[]
    @Test
    public void queryStackTraceLoggerNamesTheServiceThatFiredTheQuery() {
        Long id = postService.createPost("Observed post");

        Logger logger = (Logger) LoggerFactory.getLogger(QueryStackTraceLogger.class);
        Level originalLevel = logger.getLevel();
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
        logger.setLevel(Level.DEBUG);
        try {
            // No competing lock holder, so the first attempt acquires the
            // PESSIMISTIC_WRITE lock and fires the SELECT ... FOR UPDATE.
            retryableForumService.incrementLikes(id, 1, new AtomicInteger());
        } finally {
            logger.detachAppender(appender);
            logger.setLevel(originalLevel);
        }

        // PostgreSQL renders a PESSIMISTIC_WRITE lock as "for no key update".
        String lockingReadTrace = appender.list.stream()
            .map(ILoggingEvent::getFormattedMessage)
            .filter(message -> message.contains("for no key update"))
            .findFirst()
            .orElseThrow();

        // The logger points straight at our own method, not at the @Retry proxy
        // or the Hibernate internals that sit between them.
        assertTrue(lockingReadTrace.contains(
            "com.bookstore.forum.service.PostService.incrementLikes"));
    }
    // end::stacktrace-test[]

    // tag::stacktrace-inspector[]
    @TestConfiguration
    static class StatementInspectorConfiguration {

        @Bean
        public HibernatePropertiesCustomizer queryStackTraceLogger() {
            return properties -> properties.put(
                AvailableSettings.STATEMENT_INSPECTOR,
                new QueryStackTraceLogger("com.bookstore.forum"));
        }
    }
    // end::stacktrace-inspector[]
}
