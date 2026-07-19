package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.Tag;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.utils.hibernate.type.range.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Round-trips the PostgreSQL-specific types that Hibernate 7.3 cannot map
 * natively &mdash; {@code tsrange}, {@code interval}, {@code hstore},
 * {@code inet}, {@code citext} &mdash; via Hypersistence Utils, and exercises
 * one realistic native operator per type (range containment {@code @>}, inet
 * subnet {@code <<=}, hstore {@code exist()}, citext case-insensitive equality).
 * Every {@code PostDetails}/{@code PostComment} hangs off a root {@link Post}.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class PostgreSQLTypesTest {

    @Autowired
    private ForumService forumService;

    @BeforeEach
    public void cleanUp() {
        forumService.deleteAll();
    }

    @Test
    public void rangeIntervalAndHstoreRoundTrip() {
        PostDetails details = new PostDetails(new Post("PostgreSQL types"));
        details.setPublicationPeriod(Range.closed(
            LocalDateTime.of(2026, 1, 1, 0, 0, 0),
            LocalDateTime.of(2026, 12, 31, 0, 0, 0)));
        details.setReadTimeBudget(Duration.ofMinutes(8).plusSeconds(30));
        details.setAttributes(Map.of("difficulty", "advanced", "language", "en"));

        PostDetails loaded = forumService.findDetails(forumService.savePostWithDetails(details).getId());

        assertEquals(LocalDateTime.of(2026, 1, 1, 0, 0, 0), loaded.getPublicationPeriod().lower());
        assertEquals(LocalDateTime.of(2026, 12, 31, 0, 0, 0), loaded.getPublicationPeriod().upper());
        assertEquals(Duration.ofMinutes(8).plusSeconds(30), loaded.getReadTimeBudget());
        assertEquals("advanced", loaded.getAttributes().get("difficulty"));
        assertEquals("en", loaded.getAttributes().get("language"));
    }

    @Test
    public void rangeContainmentAndHstoreExistQueries() {
        PostDetails inWindow = new PostDetails(new Post("Active in June 2026"));
        inWindow.setPublicationPeriod(Range.closed(
            LocalDateTime.of(2026, 1, 1, 0, 0, 0),
            LocalDateTime.of(2026, 12, 31, 0, 0, 0)));
        inWindow.setAttributes(Map.of("difficulty", "advanced"));
        Long inWindowId = forumService.savePostWithDetails(inWindow).getId();

        PostDetails outOfWindow = new PostDetails(new Post("Active in 2025 only"));
        outOfWindow.setPublicationPeriod(Range.closed(
            LocalDateTime.of(2025, 1, 1, 0, 0, 0),
            LocalDateTime.of(2025, 12, 31, 0, 0, 0)));
        outOfWindow.setAttributes(Map.of("language", "en"));
        forumService.savePostWithDetails(outOfWindow);

        List<PostDetails> publishedInJune = forumService.findPublishedAt("2026-06-01 12:00:00");
        assertEquals(1, publishedInJune.size());
        assertEquals(inWindowId, publishedInJune.get(0).getId());

        List<PostDetails> withDifficulty = forumService.findWithAttribute("difficulty");
        assertEquals(1, withDifficulty.size());
        assertEquals(inWindowId, withDifficulty.get(0).getId());
    }

    @Test
    public void inetSubnetQueryFindsCommentsInsideCidr() {
        forumService.savePostWithComment(
            new PostComment(new Post("LAN thread"), "From the office LAN", "192.168.1.42"));
        forumService.savePostWithComment(
            new PostComment(new Post("Remote thread"), "From somewhere else", "10.0.0.7"));

        List<PostComment> fromLan = forumService.findFromSubnet("192.168.1.0/24");

        assertEquals(1, fromLan.size());
        assertEquals("From the office LAN", fromLan.get(0).getReview());
    }

    @Test
    public void citextLookupIsCaseInsensitive() {
        forumService.saveTag(new Tag("Hibernate"));

        assertTrue(forumService.findTagByName("hibernate").isPresent());
        assertTrue(forumService.findTagByName("HIBERNATE").isPresent());
        assertEquals("Hibernate", forumService.findTagByName("hibernate").orElseThrow().getName());
    }

    @Test
    public void citextUniquenessIsCaseInsensitive() {
        forumService.saveTag(new Tag("Hibernate"));

        assertThrows(DataIntegrityViolationException.class,
            () -> forumService.saveTag(new Tag("hibernate")));
    }
}
