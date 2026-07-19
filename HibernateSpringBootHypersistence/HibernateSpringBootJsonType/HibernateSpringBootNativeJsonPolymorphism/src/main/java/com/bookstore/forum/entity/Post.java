package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * A forum thread whose body is a polymorphic list of {@link PostContentBlock}s
 * mapped with the Hibernate-native {@code @JdbcTypeCode(SqlTypes.JSON)} (which
 * resolves to {@code jsonb} on PostgreSQL).
 *
 * <p>The companion test confirms the native mapping honors Jackson's
 * {@code @JsonTypeInfo} polymorphic type information: Hibernate passes Jackson
 * the fully resolved generic type ({@code List<PostContentBlock>}), which
 * engages the polymorphic type serializer, so each element is written with its
 * {@code "type"} discriminator and read back as the correct subtype.</p>
 */
@Entity
@Table(name = "native_polymorphic_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
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
