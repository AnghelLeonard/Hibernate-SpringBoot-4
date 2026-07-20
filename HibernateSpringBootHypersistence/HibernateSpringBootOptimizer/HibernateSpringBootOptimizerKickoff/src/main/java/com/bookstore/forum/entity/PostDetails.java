package com.bookstore.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * <strong>Deliberately suboptimal.</strong> The child side of the
 * {@code @OneToOne} has its own generated identifier and a plain
 * {@code @OneToOne} association:
 *
 * <ul>
 *   <li>no {@code @MapsId}, so the row needs a surrogate key and a separate
 *       unique constraint instead of sharing the post's identifier →
 *       {@code OneToOneWithoutMapsIdEvent};</li>
 *   <li>{@code @OneToOne} defaults to {@code EAGER}, so every time these details
 *       are loaded the post is loaded too, whether or not anyone asked →
 *       {@code EagerFetchingEvent}.</li>
 * </ul>
 */
@Entity
@Table(name = "kickoff_post_details")
public class PostDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "created_by")
    private String createdBy;

    @OneToOne
    private Post post;

    public PostDetails() {
    }

    public PostDetails(String createdBy) {
        this.createdBy = createdBy;
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
