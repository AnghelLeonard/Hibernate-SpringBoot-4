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
 * <p>The discriminator is exposed as a real (read-only) {@link #getType()} bean
 * property, and {@link JsonTypeInfo.As#EXISTING_PROPERTY} tells Jackson to use
 * that getter as the discriminator rather than adding a synthetic field. This is
 * required for the Hypersistence {@code JsonBinaryType} mapping: it serializes
 * through {@code ObjectMapper.writeValueAsString(value)} <em>without</em> the
 * declared generic type, so Jackson's polymorphic type serializer is not
 * engaged and a plain {@code @JsonTypeInfo} discriminator would be lost on
 * write. Publishing it as a getter guarantees the {@code "type"} tag is written,
 * and on read the class-level {@code property = "type"} uses it to pick the
 * subtype. (This is the same pattern as Hypersistence Utils' own
 * {@code type/json/polymorphic} tests.)</p>
 *
 * <p>Even in the Jackson 3 line used by {@code hypersistence-utils-hibernate-73}
 * the annotations still live in {@code com.fasterxml.jackson.annotation} (only
 * databind moved to {@code tools.jackson.databind}).</p>
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
