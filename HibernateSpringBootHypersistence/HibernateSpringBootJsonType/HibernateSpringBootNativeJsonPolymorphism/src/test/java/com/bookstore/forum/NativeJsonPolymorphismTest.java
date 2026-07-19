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
 * Confirms that the Hibernate-native {@code @JdbcTypeCode(SqlTypes.JSON)}
 * supports polymorphic JSON on PostgreSQL {@code jsonb}. Because Hibernate hands
 * Jackson the fully resolved generic type ({@code List<PostContentBlock>}),
 * Jackson engages its polymorphic type serializer and honors the
 * {@code @JsonTypeInfo}/{@code @JsonSubTypes} discriminator, restoring each
 * element as its concrete subtype — the same result the {@code JsonBinaryType}
 * module gets.
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class NativeJsonPolymorphismTest {

    @Autowired
    private ForumService forumService;

    @BeforeEach
    public void cleanUp() {
        forumService.deleteAll();
    }

    @Test
    public void nativeRestoresEachBlockAsItsConcreteSubtype() {
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
}
