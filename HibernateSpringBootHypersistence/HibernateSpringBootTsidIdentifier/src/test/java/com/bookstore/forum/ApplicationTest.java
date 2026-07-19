package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
public class ApplicationTest {

    @Autowired
    private ForumService forumService;

    @Test
    public void forumWorkflow() {
        Post post = forumService.newPost(
            "Is Hibernate Spring Boot 4 worth reading?",
            List.of("hibernate", "jpa")
        );

        assertNotNull(post.getId());
        assertTrue(TSID.isValid(TSID.from(post.getId()).toString()));

        List<Post> posts = forumService.findByTitle(
            "Is Hibernate Spring Boot 4 worth reading?"
        );
        assertEquals(1, posts.size());

        long postCountBeforeDelete = forumService.countPosts();
        forumService.deletePost(post.getId());
        assertEquals(postCountBeforeDelete - 1, forumService.countPosts());
    }

    @Test
    public void tsidGeneratesLongStringAndTsidTypedColumns() {
        Post post = forumService.newPost(
            "TSID supports Long, String and TSID attributes",
            List.of("tsid")
        );

        PostDetails details = forumService.findDetails(post.getId());

        // Long identifier
        assertNotNull(post.getId());
        // String @Tsid column
        assertNotNull(details.getExternalId());
        assertTrue(TSID.isValid(details.getExternalId()));
        // TSID-typed @Tsid column (mapped to bigint via the AttributeConverter)
        TSID publicId = details.getPublicId();
        assertNotNull(publicId);
        assertTrue(TSID.isValid(publicId.toString()));
    }
}
