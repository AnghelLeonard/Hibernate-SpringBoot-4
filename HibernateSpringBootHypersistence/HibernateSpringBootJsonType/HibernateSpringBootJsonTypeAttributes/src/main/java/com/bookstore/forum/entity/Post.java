package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A single entity mapping five different Java attribute types to {@code json}
 * columns with the very same {@code @Type(JsonType.class)} annotation:
 * a POJO, a Jackson {@link JsonNode}, a {@link Map}, a raw JSON {@link String}
 * and a {@link List}. This is the flexibility the Hibernate-native
 * {@code @JdbcTypeCode(SqlTypes.JSON)} mapping cannot match on its own.
 */
@Entity
@Table(name = "post")
public class Post {

    @Id
    @Tsid
    private Long id;

    private String title;

    @Type(JsonType.class)
    @Column(name = "properties", columnDefinition = "json")
    private PostProperties properties;

    @Type(JsonType.class)
    @Column(name = "metadata", columnDefinition = "json")
    private JsonNode metadata;

    @Type(JsonType.class)
    @Column(name = "attributes", columnDefinition = "json")
    private Map<String, String> attributes = new LinkedHashMap<>();

    @Type(JsonType.class)
    @Column(name = "raw_payload", columnDefinition = "json")
    private String rawPayload;

    @Type(JsonType.class)
    @Column(name = "tags", columnDefinition = "json")
    private List<String> tags = new ArrayList<>();

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

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getRawPayload() {
        return rawPayload;
    }

    public void setRawPayload(String rawPayload) {
        this.rawPayload = rawPayload;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
