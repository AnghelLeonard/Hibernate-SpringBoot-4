package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Fixed: {@code @ManyToOne} is declared {@code LAZY} instead of taking the
 * {@code EAGER} default, so loading comments no longer drags in a post per row
 * ({@code EagerFetchingEvent}). Whoever needs the post asks for it — with a
 * {@code join fetch} — and pays for it once.
 */
@Entity
@Table(name = "filtered_post_comment")
public class PostComment {

    @Id
    @GeneratedValue
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
