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
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.assertUpdateCount;
import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.reset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Documents the Hibernate-native {@code @JdbcTypeCode(SqlTypes.JSON)} mapping
 * on MySQL with Jackson 3 as the format mapper. Modern Hibernate maps all five
 * attribute types (POJO, JsonNode, Map, String, List) to {@code json} columns,
 * so the attribute-type coverage matches JsonType here. The difference shows up
 * in <em>dirty checking</em>: the native mapping issues an UPDATE on every
 * flush for a mutable POJO without {@code equals}, whereas JsonType compares
 * the JSON content and skips the no-op UPDATE (see the JsonType module).
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ForumService.class, TestDataSourceConfiguration.class})
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class NativeJsonAttributesTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void cleanUp() {
        jdbcTemplate.update("delete from post");
    }

    @Test
    public void nativeMapsEveryAttributeTypeToAJsonColumn() {
        assertEquals("json", columnType("properties"));
        assertEquals("json", columnType("metadata"));
        assertEquals("json", columnType("attributes"));
        assertEquals("json", columnType("raw_payload"));
        assertEquals("json", columnType("tags"));
    }

    @Test
    public void nativeRoundTripsEveryAttributeType() {
        Post post = new Post("Is Hibernate Spring Boot 4 worth reading?");
        post.setProperties(pinnedProperties());
        post.setMetadata(objectMapper.readTree("{\"votes\": 15, \"reactions\": [\"like\"]}"));
        post.setAttributes(Map.of("locale", "en"));
        post.setRawPayload("{\"source\": \"web\"}");
        post.setTags(List.of("hibernate", "jpa"));

        Post loaded = forumService.findById(forumService.save(post).getId());

        assertEquals("must-read", loaded.getProperties().getFlair());
        assertTrue(loaded.getProperties().isPinned());
        assertEquals(15, loaded.getMetadata().get("votes").asInt());
        assertEquals("en", loaded.getAttributes().get("locale"));
        assertEquals(List.of("hibernate", "jpa"), loaded.getTags());
    }

    @Test
    public void nativeIssuesAPhantomUpdateForAnUnchangedPojo() {
        Post post = new Post("Is Hibernate Spring Boot 4 worth reading?");
        post.setProperties(pinnedProperties());
        Long id = forumService.save(post).getId();

        // A read-only touch still flushes an UPDATE: the native mapping cannot
        // tell that the JSON content is unchanged. The JsonType module proves
        // the same scenario issues zero UPDATEs.
        reset();
        forumService.touch(id);
        assertUpdateCount(1);

        // Replacing the payload with an equal-content instance also updates.
        reset();
        forumService.update(id, p -> p.setProperties(pinnedProperties()));
        assertUpdateCount(1);
    }

    private PostProperties pinnedProperties() {
        PostProperties properties = new PostProperties("must-read");
        properties.setPinned(true);
        properties.getAttachments().add("table-of-contents.pdf");
        return properties;
    }

    private String columnType(String columnName) {
        return jdbcTemplate.queryForObject(
            """
            select data_type
            from information_schema.columns
            where table_schema = database() and table_name = 'post' and column_name = ?
            """,
            String.class,
            columnName
        );
    }
}
