package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.CodeBlock;
import com.bookstore.forum.entity.ImageBlock;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostContentBlock;
import com.bookstore.forum.entity.TextBlock;
import com.bookstore.forum.service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Proves that Hypersistence Utils' {@code JsonBinaryType} maps a polymorphic
 * list of {@link PostContentBlock}s into a PostgreSQL {@code jsonb} column and
 * restores each element as its concrete subtype, and that the stored
 * {@code jsonb} is queryable with the containment ({@code @>}) operator.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class JsonTypePolymorphismTest {

    @Autowired
    private ForumService forumService;

    @BeforeEach
    public void cleanUp() {
        forumService.deleteAll();
    }

    @Test
    public void jsonBinaryTypeRestoresEachBlockAsItsConcreteSubtype() {
        Post post = new Post("Mapping polymorphic JSON")
            .addBlock(new TextBlock("Intro paragraph"))
            .addBlock(new CodeBlock("java", "var x = 1;"))
            .addBlock(new ImageBlock("https://example.com/a.png", "a diagram"));

        Post loaded = forumService.findById(forumService.save(post).getId());

        List<PostContentBlock> body = loaded.getBody();
        assertEquals(3, body.size());

        TextBlock text = assertInstanceOf(TextBlock.class, body.get(0));
        assertEquals("Intro paragraph", text.getMarkdown());

        CodeBlock code = assertInstanceOf(CodeBlock.class, body.get(1));
        assertEquals("java", code.getLanguage());
        assertEquals("var x = 1;", code.getSource());

        ImageBlock image = assertInstanceOf(ImageBlock.class, body.get(2));
        assertEquals("https://example.com/a.png", image.getUrl());
        assertEquals("a diagram", image.getAlt());
    }

    @Test
    public void jsonbContainmentQueryFindsPostsByBlockContent() {
        forumService.save(
            new Post("Has a Java snippet")
                .addBlock(new TextBlock("look at this"))
                .addBlock(new CodeBlock("java", "int i = 0;")));
        forumService.save(
            new Post("Only prose")
                .addBlock(new TextBlock("no code here")));

        List<Post> withJava = forumService.findPostsContaining(
            "[{\"type\":\"code\",\"language\":\"java\"}]");

        assertEquals(1, withJava.size());
        assertEquals("Has a Java snippet", withJava.get(0).getTitle());
    }
}
