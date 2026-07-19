package com.bookstore.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Duration;

/**
 * Details of a {@link Post}, mapped with the PostgreSQL types Hibernate 7.3
 * supports <em>natively</em> &mdash; no Hypersistence Utils, no custom
 * {@code @Type}.
 *
 * <p>The only rich type here is {@link Duration}: with
 * {@link SqlTypes#INTERVAL_SECOND} it maps straight to a PostgreSQL
 * {@code interval second} column. This is the one type the Hypersistence Utils
 * module also offers ({@code PostgreSQLIntervalType}); the difference is that
 * the native form covers only the seconds interval sub-family, whereas the
 * Hypersistence one round-trips the full {@code interval} (day/month/year
 * components too).</p>
 *
 * <p>The Hypersistence module's other {@code PostDetails} fields have
 * <strong>no native equivalent</strong> and so are absent here: PostgreSQL
 * {@code tsrange} (no range type exists in Hibernate) and {@code hstore} (no
 * built-in mapping).</p>
 */
@Entity
@Table(name = "native_post_details")
public class PostDetails {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Post post;

    /**
     * Read-time budget mapped natively to {@code interval second} through
     * Hibernate's {@code INTERVAL_SECOND} SQL type.
     */
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    @Column(name = "read_time_budget")
    private Duration readTimeBudget;

    public PostDetails() {
    }

    public PostDetails(Post post) {
        this.post = post;
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

    public Duration getReadTimeBudget() {
        return readTimeBudget;
    }

    public void setReadTimeBudget(Duration readTimeBudget) {
        this.readTimeBudget = readTimeBudget;
    }
}
