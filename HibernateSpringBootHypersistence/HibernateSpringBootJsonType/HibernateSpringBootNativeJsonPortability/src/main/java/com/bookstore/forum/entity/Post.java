package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A forum thread whose JSON attributes are mapped with the Hibernate-native
 * {@code @JdbcTypeCode(SqlTypes.JSON)}. Because {@code SqlTypes.JSON} is a
 * dialect-aware type, schema generation needs no {@code columnDefinition}:
 * Hibernate emits the right column type per database ({@code json} on MySQL,
 * {@code jsonb} on PostgreSQL, and so on). This is where the native mapping is
 * strong; the JsonType module trades this for richer runtime semantics.
 */
@Entity
@Table(name = "native_portable_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    private PostProperties properties;

    @JdbcTypeCode(SqlTypes.JSON)
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
