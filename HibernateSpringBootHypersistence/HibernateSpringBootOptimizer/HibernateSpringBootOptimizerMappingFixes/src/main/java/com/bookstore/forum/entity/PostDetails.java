package com.bookstore.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Fixed: the details row now <em>shares</em> the post's identifier instead of
 * carrying a surrogate key of its own.
 *
 * <ul>
 *   <li><strong>{@code OneToOneWithoutMapsIdEvent}</strong> — {@code @MapsId}
 *       makes {@code id} both the primary key and the foreign key, so the extra
 *       column, its sequence and its unique index all disappear, and finding the
 *       details by post identifier needs no join.</li>
 *   <li><strong>{@code EagerFetchingEvent}</strong> — {@code @OneToOne} defaults
 *       to {@code EAGER}; declared {@code LAZY} here.</li>
 * </ul>
 */
@Entity
@Table(name = "fixed_post_details")
public class PostDetails {

    @Id
    private Long id;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "created_by")
    private String createdBy;

    // tag::mapsid[]
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Post post;
    // end::mapsid[]

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
