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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The native counterpart of {@code JsonTypeObjectMapperTest}: proves that a
 * custom Hibernate {@code FormatMapper}, selected through
 * {@code hibernate.type.json_format_mapper}, governs how the native
 * {@code @JdbcTypeCode(SqlTypes.JSON)} column is serialized. The {@code snake_case}
 * strategy stores the camelCase Java field {@code flairLabel} under the JSON key
 * {@code flair_label}, and the same mapper reads it back.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ForumService.class, TestDataSourceConfiguration.class})
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class NativeJsonFormatMapperTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void cleanUp() {
        jdbcTemplate.update("delete from format_mapper_post");
    }

    @Test
    public void customFormatMapperControlsTheStoredJsonKeys() {
        Post post = new Post("Custom FormatMapper for native JSON");
        post.setProperties(new PostProperties("must-read", true));

        Long id = forumService.save(post).getId();

        String storedJson = jdbcTemplate.queryForObject(
            "select properties from format_mapper_post where id = ?", String.class, id);

        // The custom snake_case FormatMapper renamed the JSON keys...
        assertTrue(storedJson.contains("flair_label"), storedJson);
        assertTrue(storedJson.contains("pinned_by_moderator"), storedJson);
        // ...and did NOT leave the default camelCase key.
        assertFalse(storedJson.contains("flairLabel"), storedJson);

        // The same mapper reads it back into the camelCase Java fields.
        Post loaded = forumService.findById(id);
        assertEquals("must-read", loaded.getProperties().getFlairLabel());
        assertTrue(loaded.getProperties().isPinnedByModerator());
    }
}
