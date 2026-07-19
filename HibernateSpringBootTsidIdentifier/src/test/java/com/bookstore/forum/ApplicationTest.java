package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
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
            "Is High-Performance Java Persistence worth reading?",
            List.of("hibernate", "jpa")
        );

        assertNotNull(post.getId());
        assertTrue(TSID.isValid(TSID.from(post.getId()).toString()));

        List<Post> posts = forumService.findByTitle(
            "Is High-Performance Java Persistence worth reading?"
        );
        assertEquals(1, posts.size());

        long postCountBeforeDelete = forumService.countPosts();
        forumService.deletePost(post.getId());
        assertEquals(postCountBeforeDelete - 1, forumService.countPosts());
    }
}
