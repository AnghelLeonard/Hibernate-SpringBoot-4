package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * The exact same shape as {@link Post}, but the JSON attribute is mapped with
 * the Hibernate-native {@code @JdbcTypeCode(SqlTypes.JSON)} instead of the
 * Hypersistence {@code JsonType}, so the two mappings can be compared
 * side by side.
 */
@Entity
@Table(name = "post_draft")
public class PostDraft {

    @Id
    @Tsid
    private Long id;

    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "properties")
    private PostProperties properties;

    public PostDraft() {
    }

    public PostDraft(String title) {
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

    public PostProperties getProperties() {
        return properties;
    }

    public void setProperties(PostProperties properties) {
        this.properties = properties;
    }
}
