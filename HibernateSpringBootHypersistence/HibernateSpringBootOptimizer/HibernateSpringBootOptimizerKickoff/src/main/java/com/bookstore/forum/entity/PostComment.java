package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * <strong>Deliberately suboptimal.</strong> {@code @ManyToOne} defaults to
 * {@code EAGER}, so loading a page of comments loads every parent post as well —
 * the classic N+1 that no amount of query tuning can undo, because the fetch
 * plan is baked into the mapping → {@code EagerFetchingEvent}.
 */
@Entity
@Table(name = "kickoff_post_comment")
public class PostComment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Post post;

    private String review;

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

    public void setReview(String review) {
        this.review = review;
    }
}
