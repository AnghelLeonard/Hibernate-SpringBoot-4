package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostStatus;
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
 * Round-trips every Hypersistence Utils array type against PostgreSQL, including
 * the two that the Hibernate-native array mapping cannot handle (native enum
 * arrays and multidimensional arrays), and exercises the PostgreSQL array
 * operators {@code = ANY(array)} and {@code &&} (overlap).
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class HypersistenceArrayTypesTest {

    private static final UUID EDITOR_1 = UUID.fromString("c65a3bcb-8b36-46d4-bddb-ae96ad016eb1");
    private static final UUID EDITOR_2 = UUID.fromString("72e95717-5294-4c15-aa64-a3631cf9a800");

    @Autowired
    private ForumService forumService;

    @BeforeEach
    public void cleanUp() {
        forumService.deleteAll();
    }

    @Test
    public void everyArrayTypeRoundTrips() {
        Post post = new Post("Mapping PostgreSQL arrays");
        post.setKeywords(new String[]{"hibernate", "arrays"});
        post.setTopics(List.of("mapping", "types"));
        post.setScores(new int[]{5, 4, 5});
        post.setPublicationDates(new LocalDate[]{LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1)});
        post.setEditorIds(new UUID[]{EDITOR_1, EDITOR_2});
        post.setStatusHistory(new PostStatus[]{PostStatus.DRAFT, PostStatus.PUBLISHED, PostStatus.ARCHIVED});
        post.setRatingMatrix(new Integer[][]{{5, 4}, {3, 5}, {2, 1}});

        Post loaded = forumService.findById(forumService.save(post).getId());

        assertArrayEquals(new String[]{"hibernate", "arrays"}, loaded.getKeywords());
        assertEquals(List.of("mapping", "types"), loaded.getTopics());
        assertArrayEquals(new int[]{5, 4, 5}, loaded.getScores());
        assertArrayEquals(new LocalDate[]{LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1)},
            loaded.getPublicationDates());
        assertArrayEquals(new UUID[]{EDITOR_1, EDITOR_2}, loaded.getEditorIds());
        // native PostgreSQL enum array
        assertArrayEquals(new PostStatus[]{PostStatus.DRAFT, PostStatus.PUBLISHED, PostStatus.ARCHIVED},
            loaded.getStatusHistory());
        // multidimensional array (assertArrayEquals compares nested arrays deeply)
        assertArrayEquals(new Integer[][]{{5, 4}, {3, 5}, {2, 1}}, loaded.getRatingMatrix());
    }

    @Test
    public void arrayOperatorQueriesFindPosts() {
        Post hibernatePost = new Post("Has hibernate keyword");
        hibernatePost.setKeywords(new String[]{"hibernate", "jpa"});
        forumService.save(hibernatePost);

        Post springPost = new Post("Has spring keyword");
        springPost.setKeywords(new String[]{"spring", "boot"});
        forumService.save(springPost);

        // = ANY(array)
        List<Post> withHibernate = forumService.findByKeyword("hibernate");
        assertEquals(1, withHibernate.size());
        assertEquals("Has hibernate keyword", withHibernate.get(0).getTitle());

        // && (array overlap)
        List<Post> overlapping = forumService.findByAnyKeyword("boot", "quarkus");
        assertEquals(1, overlapping.size());
        assertEquals("Has spring keyword", overlapping.get(0).getTitle());
    }
}
