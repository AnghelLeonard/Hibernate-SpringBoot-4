package com.bookstore.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A forum thread whose {@code properties} JSON column is mapped with the
 * Hibernate-native {@code @JdbcTypeCode(SqlTypes.JSON)}. The JSON is serialized
 * by whichever {@code FormatMapper} is configured through
 * {@code hibernate.type.json_format_mapper}; here that is the custom
 * {@code snake_case} {@link com.bookstore.forum.config.SnakeCaseJsonFormatMapper}.
 */
@Entity
@Table(name = "format_mapper_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "properties")
    private PostProperties properties;

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
}
