package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A forum thread whose JSON attributes are mapped with {@code @Type(JsonType.class)}
 * and no {@code columnDefinition}. The JsonType binding (Jackson serialization,
 * content-based dirty checking, per-dialect JDBC type) is database-agnostic, so
 * the very same Java attribute types run unchanged on every database. The
 * {@code columnDefinition} is only needed when Hibernate generates the schema
 * itself; in production the schema comes from a migration tool such as Flyway,
 * so this module creates the table before the context bootstraps (via
 * {@code spring.sql.init}) and leaves {@code ddl-auto=none}. A client-side
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

    // tag::json-no-columndef[]
    @Type(JsonType.class)
    private PostProperties properties;

    @Type(JsonType.class)
    private Map<String, String> attributes = new LinkedHashMap<>();
    // end::json-no-columndef[]

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
