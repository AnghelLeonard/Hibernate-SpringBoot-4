package com.bookstore.forum.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * A single block in a forum post's body. A post body is a heterogeneous list of
 * these blocks (paragraphs, images, code snippets) serialized into one JSON
 * column. The Jackson {@link JsonTypeInfo}/{@link JsonSubTypes} pair embeds a
 * {@code "type"} discriminator so each element deserializes back into its
 * concrete subtype.
 *
 * <p>The Hibernate-native {@code @JdbcTypeCode(SqlTypes.JSON)} mapping passes the
 * fully resolved generic type ({@code List<PostContentBlock>}) to Jackson, which
 * engages Jackson's polymorphic type serializer and honors {@code @JsonTypeInfo}
 * even without an explicit discriminator getter. The discriminator is
 * nevertheless published as a real (read-only) {@link #getType()} property with
 * {@link JsonTypeInfo.As#EXISTING_PROPERTY} so this hierarchy is identical to the
 * {@code JsonBinaryType} module's — where the getter is <em>required</em> — which
 * lets the two mappings be compared off the same model.</p>
 *
 * <p>Even in the Jackson 3 line used here, the annotations still live in
 * {@code com.fasterxml.jackson.annotation} (only databind moved to
 * {@code tools.jackson.databind}).</p>
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(name = TextBlock.TYPE, value = TextBlock.class),
    @JsonSubTypes.Type(name = ImageBlock.TYPE, value = ImageBlock.class),
    @JsonSubTypes.Type(name = CodeBlock.TYPE, value = CodeBlock.class)
})
public abstract sealed class PostContentBlock
        implements Serializable
        permits TextBlock, ImageBlock, CodeBlock {

    public abstract String getType();
}
