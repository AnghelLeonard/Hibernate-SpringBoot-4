package com.bookstore.forum.entity;

import tools.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.id.Tsid;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "post_comment")
public class PostComment {

    @Id
    @Tsid
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String review;

    @Type(JsonType.class)
    @Column(name = "attributes", columnDefinition = "json")
    private JsonNode attributes;

    public PostComment() {
    }

    public PostComment(String review) {
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getReview() {
        return review;
    }

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
    }
}
