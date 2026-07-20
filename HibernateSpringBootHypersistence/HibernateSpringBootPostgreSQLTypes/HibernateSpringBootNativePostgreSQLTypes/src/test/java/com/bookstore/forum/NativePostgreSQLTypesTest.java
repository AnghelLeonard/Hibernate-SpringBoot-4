package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.Tag;
import com.bookstore.forum.service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Confirms the PostgreSQL-specific types Hibernate 7.3 maps <em>natively</em> —
 * {@code interval second} for {@link Duration} ({@code INTERVAL_SECOND}),
 * {@code inet} for a plain {@link InetAddress}, and a {@code citext} column via
 * {@code columnDefinition} — round-trip and answer the same native operators
 * ({@code <<=} subnet match, case-insensitive equality) as the Hypersistence
 * Utils module, with no extra library. Every child hangs off a root {@link Post}.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class NativePostgreSQLTypesTest {

    @Autowired
    private ForumService forumService;

    @BeforeEach
    public void cleanUp() {
        forumService.deleteAll();
    }

    @Test
    public void durationRoundTripsViaNativeIntervalSecond() {
        PostDetails details = new PostDetails(new Post("Native interval"));
        details.setReadTimeBudget(Duration.ofMinutes(8).plusSeconds(30));

        PostDetails loaded = forumService.findDetails(forumService.savePostWithDetails(details).getId());

        assertEquals(Duration.ofMinutes(8).plusSeconds(30), loaded.getReadTimeBudget());
    }

    @Test
    public void inetSubnetQueryFindsCommentsInsideCidr() throws UnknownHostException {
        forumService.savePostWithComment(new PostComment(
            new Post("LAN thread"), "From the office LAN", InetAddress.getByName("192.168.1.42")));
        forumService.savePostWithComment(new PostComment(
            new Post("Remote thread"), "From somewhere else", InetAddress.getByName("10.0.0.7")));

        List<PostComment> fromLan = forumService.findFromSubnet("192.168.1.0/24");

        assertEquals(1, fromLan.size());
        assertEquals("From the office LAN", fromLan.get(0).getReview());
    }

    @Test
    public void nativeCitextDerivedLookupIsCaseSensitive() {
        forumService.saveTag(new Tag("Hibernate"));

        // Exact case still matches...
        assertTrue(forumService.findTagByName("Hibernate").isPresent());
        // ...but a differently-cased lookup does NOT, because Hibernate binds the
        // parameter as varchar (no native citext type). This is the native
        // mapping's limitation vs Hypersistence Utils' PostgreSQLCITextType.
        assertFalse(forumService.findTagByName("hibernate").isPresent());
    }

    @Test
    public void nativeCitextLookupIsCaseInsensitiveWithExplicitCast() {
        forumService.saveTag(new Tag("Hibernate"));

        // Casting the bind parameter to citext restores case-insensitive matching.
        assertTrue(forumService.findTagByNameCaseInsensitively("hibernate").isPresent());
        assertTrue(forumService.findTagByNameCaseInsensitively("HIBERNATE").isPresent());
        assertEquals("Hibernate",
            forumService.findTagByNameCaseInsensitively("hibernate").orElseThrow().getName());
    }

    @Test
    public void nativeCitextUniquenessIsCaseInsensitive() {
        // The unique constraint IS case-insensitive — that lives in the column
        // type, independent of how Hibernate binds query parameters.
        forumService.saveTag(new Tag("Hibernate"));

        assertThrows(DataIntegrityViolationException.class,
            () -> forumService.saveTag(new Tag("hibernate")));
    }
}
