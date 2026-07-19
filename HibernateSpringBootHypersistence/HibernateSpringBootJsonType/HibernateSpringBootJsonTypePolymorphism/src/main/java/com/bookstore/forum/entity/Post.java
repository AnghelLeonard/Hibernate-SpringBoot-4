package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * A forum thread whose body is a polymorphic list of {@link PostContentBlock}s
 * mapped with Hypersistence Utils' {@code @Type(JsonBinaryType.class)} into a
 * PostgreSQL {@code jsonb} column.
 *
 * <p>{@code JsonBinaryType} is the PostgreSQL-specific mapping that binds the
 * value with {@code setObject(..., Types.OTHER)}, which the driver accepts for a
 * {@code jsonb} column; the generic {@code JsonType} instead round-trips a
 * {@code PGobject} and corrupts a {@code jsonb} column on write. Each element is
 * written with its {@code "type"} tag (published by {@link PostContentBlock#getType()})
 * and read back as the correct concrete subtype.</p>
 *
 * <p>A GIN index on the {@code body} column makes {@code jsonb} containment
 * ({@code @>}) queries index-friendly; it is created in {@code schema.sql}
 * because Hibernate's schema generation cannot emit a {@code USING gin} index.</p>
 */
@Entity
@Table(name = "polymorphic_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<PostContentBlock> body = new ArrayList<>();

    public Post() {
    }

    public Post(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PostContentBlock> getBody() {
        return body;
    }

    public void setBody(List<PostContentBlock> body) {
        this.body = body;
    }

    public Post addBlock(PostContentBlock block) {
        body.add(block);
        return this;
    }
}
