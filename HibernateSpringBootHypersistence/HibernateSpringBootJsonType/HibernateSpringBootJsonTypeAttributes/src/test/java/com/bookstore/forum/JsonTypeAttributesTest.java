package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostProperties;
import com.bookstore.forum.repository.PostSummary;
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
 * Proves {@code @Type(JsonType.class)} maps five different Java attribute
 * types &mdash; POJO, JsonNode, Map, String and List &mdash; each to a MySQL
 * {@code json} column, with content-aware dirty checking on top.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ForumService.class, TestDataSourceConfiguration.class})
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class JsonTypeAttributesTest {

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
    public void everyAttributeTypeMapsToAJsonColumn() {
        assertEquals("json", columnType("properties"));
        assertEquals("json", columnType("metadata"));
        assertEquals("json", columnType("attributes"));
        assertEquals("json", columnType("raw_payload"));
        assertEquals("json", columnType("tags"));
    }

    @Test
    public void everyAttributeTypeRoundTrips() {
        Post post = new Post("Is Hibernate Spring Boot 4 worth reading?");
        post.setProperties(pinnedProperties());
        post.setMetadata(objectMapper.readTree("{\"votes\": 15, \"reactions\": [\"like\"]}"));
        post.setAttributes(Map.of("locale", "en", "channel", "web"));
        post.setRawPayload("{\"source\": \"web\"}");
        post.setTags(List.of("hibernate", "jpa", "json"));

        Post loaded = forumService.findById(forumService.save(post).getId());

        assertEquals("must-read", loaded.getProperties().getFlair());
        assertTrue(loaded.getProperties().isPinned());
        assertEquals(List.of("table-of-contents.pdf"), loaded.getProperties().getAttachments());
        assertEquals(15, loaded.getMetadata().get("votes").asInt());
        assertEquals("en", loaded.getAttributes().get("locale"));
        assertEquals("web", loaded.getAttributes().get("channel"));
        assertEquals(List.of("hibernate", "jpa", "json"), loaded.getTags());
    }

    @Test
    public void rawStringPayloadIsStoredAsQueryableJson() {
        Post post = new Post("Is Hibernate Spring Boot 4 worth reading?");
        post.setRawPayload("{\"source\": \"web\", \"spam_score\": 0.05}");

        forumService.save(post);

        String source = jdbcTemplate.queryForObject(
            "select json_unquote(json_extract(raw_payload, '$.source')) from post",
            String.class
        );
        assertEquals("web", source);
    }

    @Test
    public void jsonTypeSkipsTheUpdateWhenTheContentIsUnchanged() {
        Post post = new Post("Is Hibernate Spring Boot 4 worth reading?");
        post.setProperties(pinnedProperties());
        Long id = forumService.save(post).getId();

        reset();
        forumService.touch(id);
        assertUpdateCount(0);

        reset();
        forumService.update(id, p -> p.setProperties(pinnedProperties()));
        assertUpdateCount(0);

        reset();
        forumService.update(id, p -> p.getProperties().setFlair("hot"));
        assertUpdateCount(1);
    }

    @Test
    public void dtoProjectionReadsJsonAttributeViaJsonExtract() {
        Post post = new Post("Is Hibernate Spring Boot 4 worth reading?");
        post.setRawPayload("{\"source\": \"web\", \"spam_score\": 0.05}");
        Long id = forumService.save(post).getId();

        PostSummary summary = forumService.findSummary(id);

        assertEquals("Is Hibernate Spring Boot 4 worth reading?", summary.getTitle());
        assertEquals("web", summary.getSource());
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
