package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostProperties;
import com.bookstore.forum.service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Proves that a custom Jackson {@code ObjectMapper}, supplied to {@code JsonType}
 * through the {@code hypersistence.utils.jackson.object.mapper} property, governs
 * how JSON columns are serialized: the {@code snake_case} strategy renames the
 * keys, {@code createdOn} is stored as an ISO-8601 string (Jackson 3 default),
 * and the {@code null} {@code editedOn} is dropped by {@code NON_NULL} inclusion.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ForumService.class, TestDataSourceConfiguration.class})
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class JsonTypeObjectMapperTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void cleanUp() {
        jdbcTemplate.update("delete from object_mapper_post");
    }

    @Test
    public void customObjectMapperControlsTheStoredJson() {
        // tag::save[]
        Post post = new Post("Custom ObjectMapper for JsonType");
        post.setProperties(
            new PostProperties(
                "must-read", true,
                OffsetDateTime.of(2026, 8, 18, 9, 45, 0, 0, ZoneOffset.UTC)
            )
        );
        // editedOn is deliberately left null.

        Long id = forumService.save(post).getId();
        // end::save[]

        String storedJson = jdbcTemplate.queryForObject(
            "select properties from object_mapper_post where id = ?", String.class, id);

        // The snake_case strategy renamed the keys (and left no camelCase key behind)...
        assertTrue(storedJson.contains("flair_label"), storedJson);
        assertTrue(storedJson.contains("pinned_by_moderator"), storedJson);
        assertTrue(storedJson.contains("created_on"), storedJson);
        assertFalse(storedJson.contains("flairLabel"), storedJson);
        // ...createdOn is an ISO-8601 string, not a numeric timestamp (Jackson 3 default)...
        assertTrue(storedJson.contains("2026-08-18T09:45:00Z"), storedJson);
        // ...and the null editedOn was dropped by NON_NULL inclusion.
        assertFalse(storedJson.contains("edited_on"), storedJson);

        // The same mapper reads it back into the camelCase Java fields.
        Post loaded = forumService.findById(id);
        assertEquals("must-read", loaded.getProperties().getFlairLabel());
        assertTrue(loaded.getProperties().isPinnedByModerator());
        assertEquals(OffsetDateTime.of(2026, 8, 18, 9, 45, 0, 0, ZoneOffset.UTC),
            loaded.getProperties().getCreatedOn());
        assertNull(loaded.getProperties().getEditedOn());
    }
}
