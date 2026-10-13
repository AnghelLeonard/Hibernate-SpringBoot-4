package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The child side of the aggregate. The {@code post} association is {@code LAZY},
 * exactly as it should be, so the N+1 we trigger in {@code ForumService} lives
 * in application code — in a loop that walks the lazy association — not in the
 * mapping.
 */
@Entity
@Table(name = "forum_post_comment")
public class PostComment {

    @Id
    @GeneratedValue
    private Long id;

    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostComment() {
    }

    public PostComment(String review) {
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public String getReview() {
        return review;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
