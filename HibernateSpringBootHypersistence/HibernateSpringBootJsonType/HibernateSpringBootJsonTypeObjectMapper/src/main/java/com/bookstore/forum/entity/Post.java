package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

/**
 * A forum thread whose {@code properties} JSON column is serialized by a custom
 * Jackson {@code ObjectMapper} (the {@code snake_case} one supplied by
 * {@link com.bookstore.forum.config.SnakeCaseObjectMapperSupplier}). The mapping
 * itself is the ordinary {@code @Type(JsonType.class)}; only the ObjectMapper
 * behind it is customized, through the {@code hypersistence.utils.jackson.object.mapper}
 * property.
 */
@Entity
@Table(name = "object_mapper_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
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
