package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A forum thread whose JSON attributes are mapped with {@code @Type(JsonType.class)}.
 * The JsonType binding (Jackson serialization, content-based dirty checking) is
 * database-agnostic, so the very same Java attribute types run unchanged on
 * every database. Because JsonType reports a generic {@code OTHER} JDBC type,
 * schema generation needs a {@code columnDefinition}; {@code json} is shared by
 * MySQL and PostgreSQL, which is what this module runs against. A client-side
 * {@code @Tsid} identifier keeps the entity free of database-specific identity
 * or sequence strategies too.
 */
@Entity
@Table(name = "portable_post")
public class Post {

    @Id
    @Tsid
    private Long id;

    private String title;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private PostProperties properties;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, String> attributes = new LinkedHashMap<>();

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

    public PostProperties getProperties() {
        return properties;
    }

    public void setProperties(PostProperties properties) {
        this.properties = properties;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
