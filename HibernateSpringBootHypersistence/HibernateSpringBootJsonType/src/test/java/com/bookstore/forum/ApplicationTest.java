package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostProperties;
import com.bookstore.forum.service.ForumService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
public class ApplicationTest {

    @Autowired
    private ForumService forumService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void forumWorkflow() throws Exception {
        PostProperties properties = new PostProperties("must-read");
        properties.setPinned(true);

        Post post = forumService.newPost(
            "Is Hibernate Spring Boot 4 worth reading?",
            properties
        );
        assertNotNull(post.getId());

        forumService.addComment(
            post.getId(),
            "Just finished the JSON chapter, excellent!",
            objectMapper.readTree("{\"votes\": 15}")
        );

        forumService.addDetails(
            post.getId(),
            "vlad",
            "{\"source\": \"web\"}",
            Map.of("locale", "en")
        );

        Post loadedPost = forumService.findById(post.getId());
        assertEquals("must-read", loadedPost.getProperties().getFlair());
        assertTrue(loadedPost.getProperties().isPinned());

        List<Long> mustReadPostIds = forumService.findIdsByFlair("must-read");
        assertTrue(mustReadPostIds.contains(post.getId()));
    }
}
