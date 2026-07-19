package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostDraft;
import com.bookstore.forum.entity.PostProperties;
import com.bookstore.forum.service.ForumService;
import tools.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.Map;

import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.assertUpdateCount;
import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.reset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Data-access and service-layer slice test comparing the Hypersistence
 * {@code JsonType} with the Hibernate-native {@code @JdbcTypeCode(SqlTypes.JSON)}
 * mapping. Runs without a test-managed transaction so every service call
 * commits and its SQL statements are observable at the DataSource proxy level.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ForumService.class, TestDataSourceConfiguration.class})
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PostJsonMappingTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void cleanUp() {
        jdbcTemplate.update("delete from post_comment");
        jdbcTemplate.update("delete from post_details");
        jdbcTemplate.update("delete from post");
        jdbcTemplate.update("delete from post_draft");
    }

    @Test
    public void bothMappingsProduceAJsonColumn() {
        assertEquals("json", columnType("post", "properties"));
        assertEquals("json", columnType("post_draft", "properties"));

        assertEquals("json", columnType("post_details", "metadata"));
        assertEquals("json", columnType("post_details", "attributes"));
        assertEquals("json", columnType("post_comment", "attributes"));

        assertEquals("varchar", columnType("post", "title"));
    }

    @Test
    public void jsonPayloadRoundTrip() {
        PostProperties properties = new PostProperties("must-read");
        properties.setPinned(true);
        properties.getAttachments().add("table-of-contents.pdf");

        Post post = forumService.newPost(
            "Is Hibernate Spring Boot 4 worth reading?",
            properties
        );
        PostDraft draft = forumService.newDraft(
            "Draft: What is new in Spring Boot 4?",
            properties.copy()
        );

        for (PostProperties loadedProperties : List.of(
                forumService.findById(post.getId()).getProperties(),
                forumService.findDraftById(draft.getId()).getProperties())) {
            assertEquals("must-read", loadedProperties.getFlair());
            assertTrue(loadedProperties.isPinned());
            assertEquals(List.of("table-of-contents.pdf"), loadedProperties.getAttachments());
        }
    }

    @Test
    public void jsonTypeSkipsTheUpdateWhenTheContentIsUnchanged() {
        Post post = forumService.newPost(
            "Is Hibernate Spring Boot 4 worth reading?",
            new PostProperties("must-read")
        );

        reset();
        forumService.touchPost(post.getId());
        assertUpdateCount(0);

        reset();
        forumService.replaceProperties(post.getId(), new PostProperties("must-read"));
        assertUpdateCount(0);

        reset();
        forumService.replaceProperties(post.getId(), new PostProperties("hot"));
        assertUpdateCount(1);
    }

    @Test
    public void nativeJsonUpdateBehaviorOnUnchangedContent() {
        PostDraft draft = forumService.newDraft(
            "Draft: What is new in Spring Boot 4?",
            new PostProperties("must-read")
        );

        reset();
        forumService.touchDraft(draft.getId());
        assertUpdateCount(1);

        reset();
        forumService.replaceDraftProperties(draft.getId(), new PostProperties("must-read"));
        assertUpdateCount(1);

        reset();
        forumService.replaceDraftProperties(draft.getId(), new PostProperties("hot"));
        assertUpdateCount(1);
    }

    @Test
    public void inPlaceMutationOfNestedJsonPropertiesIsDetected() {
        Post post = forumService.newPost(
            "Is Hibernate Spring Boot 4 worth reading?",
            new PostProperties("must-read")
        );

        reset();
        forumService.updateProperties(post.getId(), properties ->
            properties.getAttachments().add("sample-chapter.pdf")
        );
        assertUpdateCount(1);

        assertEquals(
            List.of("sample-chapter.pdf"),
            forumService.findById(post.getId()).getProperties().getAttachments()
        );
    }

    @Test
    public void jsonNodeAttributesAreSchemaless() throws Exception {
        Post post = forumService.newPost(
            "Is Hibernate Spring Boot 4 worth reading?",
            new PostProperties("must-read")
        );

        forumService.addComment(
            post.getId(),
            "Just finished the JSON chapter, excellent!",
            objectMapper.readTree("{\"votes\": 15, \"reactions\": [\"like\", \"insightful\"]}")
        );

        Integer votes = jdbcTemplate.queryForObject(
            "select json_extract(attributes, '$.votes') from post_comment",
            Integer.class
        );
        assertEquals(15, votes);
    }

    @Test
    public void rawStringJsonColumnRemainsQueryable() {
        Post post = forumService.newPost(
            "Is Hibernate Spring Boot 4 worth reading?",
            new PostProperties("must-read")
        );

        forumService.addDetails(
            post.getId(),
            "vlad",
            "{\"source\": \"web\", \"spam_score\": 0.05}",
            Map.of("locale", "en")
        );

        String source = jdbcTemplate.queryForObject(
            "select json_unquote(json_extract(metadata, '$.source')) from post_details",
            String.class
        );
        assertEquals("web", source);

        String locale = jdbcTemplate.queryForObject(
            "select json_unquote(json_extract(attributes, '$.locale')) from post_details",
            String.class
        );
        assertEquals("en", locale);
    }

    private String columnType(String tableName, String columnName) {
        return jdbcTemplate.queryForObject(
            """
            select data_type
            from information_schema.columns
            where table_schema = database() and table_name = ? and column_name = ?
            """,
            String.class,
            tableName,
            columnName
        );
    }
}
