package com.bookstore.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.net.InetAddress;

/**
 * A comment on a {@link Post}, storing the commenter's IP in a PostgreSQL
 * {@code inet} column <em>natively</em>. Hibernate 7.3 registers a baseline
 * {@code InetAddressJavaType} whose recommended JDBC type is {@code INET}, so a
 * plain {@link InetAddress} field maps straight to {@code inet} &mdash; no
 * annotation and no {@code columnDefinition} needed. The same {@code inet}
 * column type still answers subnet operators such as {@code <<=}.
 *
 * <p>Contrast with the Hypersistence Utils module, which maps the same column
 * through {@code PostgreSQLInetType} onto its own {@code Inet} wrapper type.</p>
 */
@Entity
@Table(name = "native_post_comment")
public class PostComment {

    @Id
    @GeneratedValue
    private Long id;

    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private InetAddress remoteAddress;

    public PostComment() {
    }

    public PostComment(Post post, String review, InetAddress remoteAddress) {
        this.post = post;
        this.review = review;
        this.remoteAddress = remoteAddress;
    }

    public Long getId() {
        return id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public InetAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
