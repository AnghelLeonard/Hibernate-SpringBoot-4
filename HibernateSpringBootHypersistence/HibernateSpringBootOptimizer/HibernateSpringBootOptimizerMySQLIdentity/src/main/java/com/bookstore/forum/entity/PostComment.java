package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The MySQL variant of the fixed comment: everything the previous item fixed is
 * still fixed, and the identifier is an {@code IDENTITY} column, as MySQL
 * requires.
 */
@Entity
@Table(name = "identity_post_comment")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
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
