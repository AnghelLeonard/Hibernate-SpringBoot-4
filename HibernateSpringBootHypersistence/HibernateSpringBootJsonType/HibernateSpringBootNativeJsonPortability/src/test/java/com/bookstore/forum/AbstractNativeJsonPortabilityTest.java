package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostProperties;
import com.bookstore.forum.service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The identical round-trip assertions run against every database. The native
 * {@code SqlTypes.JSON} mapping needs no {@code columnDefinition}: Hibernate's
 * dialect emits the correct JSON column type for whichever database the
 * concrete subclass targets.
 */
@SpringBootTest
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
abstract class AbstractNativeJsonPortabilityTest {

    @Autowired
    private ForumService forumService;

    @BeforeEach
    public void cleanUp() {
        forumService.deleteAll();
    }

    @Test
    public void nativeJsonMappingRoundTripsUnchanged() {
        Post post = new Post("Is Hibernate Spring Boot 4 worth reading?");
        PostProperties properties = new PostProperties("must-read");
        properties.setPinned(true);
        post.setProperties(properties);
        post.setAttributes(Map.of("locale", "en", "channel", "web"));

        Post loaded = forumService.findById(forumService.save(post).getId());

        assertEquals("must-read", loaded.getProperties().getFlair());
        assertTrue(loaded.getProperties().isPinned());
        assertEquals("en", loaded.getAttributes().get("locale"));
        assertEquals("web", loaded.getAttributes().get("channel"));
    }
}
