package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.tsid.TSID;
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

import java.time.Instant;
import java.util.List;

import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.assertInsertCount;
import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.assertSelectCount;
import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.reset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Data-access and service-layer slice test. Runs without a test-managed
 * transaction so that the service-level transactions commit and the JDBC
 * batching behavior can be observed at the DataSource proxy level.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ForumService.class, TestDataSourceConfiguration.class})
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PostBatchInsertTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void cleanUp() {
        jdbcTemplate.update("delete from post_comment");
        jdbcTemplate.update("delete from post_details");
        jdbcTemplate.update("delete from post_tag");
        jdbcTemplate.update("delete from tag");
        jdbcTemplate.update("delete from post");
    }

    @Test
    public void clientSideTsidGenerationKeepsJdbcBatchingWorking() {
        reset();

        List<Post> posts = forumService.insertPosts(30);

        assertSelectCount(0);
        assertInsertCount(1);

        for (int i = 1; i < posts.size(); i++) {
            assertTrue(posts.get(i - 1).getId() < posts.get(i).getId());
        }
    }

    @Test
    public void tsidIdentifierEncodesTheCreationTime() {
        Instant beforePersist = Instant.now();

        Post post = forumService.newPost(
            "Which book should I read first?",
            List.of("hibernate")
        );

        TSID tsid = TSID.from(post.getId());

        assertFalse(tsid.getInstant().isBefore(beforePersist.minusSeconds(1)));
        assertFalse(tsid.getInstant().isAfter(Instant.now().plusSeconds(1)));
    }

    @Test
    public void aggregatePersistAssignsTsidValuesOnAllLevels() {
        Post post = forumService.newPost(
            "Is Hibernate Spring Boot 4 worth reading?",
            List.of("hibernate", "jpa")
        );

        assertNotNull(post.getId());
        assertEquals(2, post.getTags().size());
        post.getTags().forEach(tag -> assertNotNull(tag.getId()));

        String externalId = post.getDetails().getExternalId();
        assertEquals(13, externalId.length());
        assertTrue(TSID.isValid(externalId));

        Post postWithComments = forumService.findByIdWithComments(post.getId());
        assertEquals(2, postWithComments.getComments().size());
        postWithComments.getComments().forEach(comment -> assertNotNull(comment.getId()));
    }
}
