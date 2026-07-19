package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Confirms the Hibernate-native array mapping round-trips the simple,
 * single-dimension array types on PostgreSQL with no extra annotation, and that
 * the same {@code = ANY(array)} operator works against a natively-mapped column.
 * Native enum arrays and multidimensional arrays are covered only by the
 * Hypersistence Utils module.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class NativeArrayTypesTest {

    private static final UUID EDITOR_1 = UUID.fromString("c65a3bcb-8b36-46d4-bddb-ae96ad016eb1");
    private static final UUID EDITOR_2 = UUID.fromString("72e95717-5294-4c15-aa64-a3631cf9a800");

    @Autowired
    private ForumService forumService;

    @BeforeEach
    public void cleanUp() {
        forumService.deleteAll();
    }

    @Test
    public void nativeArrayTypesRoundTrip() {
        Post post = new Post("Mapping PostgreSQL arrays natively");
        post.setKeywords(new String[]{"hibernate", "arrays"});
        post.setTopics(List.of("mapping", "types"));
        post.setScores(new Integer[]{5, 4, 5});
        post.setPublicationDates(new LocalDate[]{LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1)});
        post.setEditorIds(new UUID[]{EDITOR_1, EDITOR_2});

        Post loaded = forumService.findById(forumService.save(post).getId());

        assertArrayEquals(new String[]{"hibernate", "arrays"}, loaded.getKeywords());
        assertEquals(List.of("mapping", "types"), loaded.getTopics());
        assertArrayEquals(new Integer[]{5, 4, 5}, loaded.getScores());
        assertArrayEquals(new LocalDate[]{LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1)},
            loaded.getPublicationDates());
        assertArrayEquals(new UUID[]{EDITOR_1, EDITOR_2}, loaded.getEditorIds());
    }

    @Test
    public void anyOperatorQueryFindsPostsOnANativelyMappedArray() {
        Post hibernatePost = new Post("Has hibernate keyword");
        hibernatePost.setKeywords(new String[]{"hibernate", "jpa"});
        forumService.save(hibernatePost);

        Post springPost = new Post("Has spring keyword");
        springPost.setKeywords(new String[]{"spring", "boot"});
        forumService.save(springPost);

        List<Post> withHibernate = forumService.findByKeyword("hibernate");

        assertEquals(1, withHibernate.size());
        assertEquals("Has hibernate keyword", withHibernate.get(0).getTitle());
    }
}
