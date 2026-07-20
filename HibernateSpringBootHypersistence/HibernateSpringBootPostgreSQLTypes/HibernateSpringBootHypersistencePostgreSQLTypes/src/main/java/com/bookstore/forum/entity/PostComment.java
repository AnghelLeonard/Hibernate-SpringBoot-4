package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.type.basic.Inet;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLInetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

/**
 * A comment on a {@link Post}, storing the commenter's IP address in a
 * PostgreSQL {@code inet} column via {@link PostgreSQLInetType}. The column type
 * unlocks IP/subnet operators such as {@code <<=} (is-contained-within-subnet)
 * that a plain {@code varchar} could not answer without a full scan and string
 * parsing.
 *
 * <p>The comment belongs to a {@code Post} through a {@code @ManyToOne} (LAZY)
 * association &mdash; a comment cannot exist without the thread it comments on.
 * Hibernate <em>also</em> supports {@code inet} natively (a plain
 * {@link java.net.InetAddress} field maps straight to {@code inet}); the native
 * module shows that variant.</p>
 */
@Entity
@Table(name = "post_comment")
public class PostComment {

    @Id
    @GeneratedValue
    private Long id;

    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Type(PostgreSQLInetType.class)
    @Column(name = "remote_address", columnDefinition = "inet")
    private Inet remoteAddress;

    public PostComment() {
    }

    public PostComment(Post post, String review, String remoteAddress) {
        this.post = post;
        this.review = review;
        this.remoteAddress = new Inet(remoteAddress);
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

    public Inet getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(Inet remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
