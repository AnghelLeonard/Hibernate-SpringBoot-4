package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Details of a {@link Post}, mapped with the PostgreSQL-specific types that
 * Hibernate 7.3 has <em>no built-in mapping</em> for and that Hypersistence
 * Utils supplies:
 *
 * <ul>
 *   <li>{@link Range} over a {@code tsrange} column ({@code publicationPeriod})
 *       via {@link PostgreSQLRangeType} &mdash; a window with inclusive/exclusive
 *       bounds and open-ended {@code infinity}. Hibernate ships no range type.</li>
 *   <li>A {@link Duration} mapped to a full PostgreSQL {@code interval}
 *       ({@code readTimeBudget}) via {@link PostgreSQLIntervalType}. This is the
 *       one type here the native module can also express, through
 *       {@code INTERVAL_SECOND} &mdash; see the native counterpart.</li>
 *   <li>A {@code Map<String, String>} over an {@code hstore} column
 *       ({@code attributes}) via
 *       {@link io.hypersistence.utils.hibernate.type.basic.PostgreSQLHStoreType}.
 *       Hibernate has no built-in {@code hstore} mapping.</li>
 * </ul>
 *
 * <p>The row shares its primary key with its owning {@link Post} through
 * {@code @OneToOne @MapsId} (LAZY) &mdash; the Hypersistence Optimizer-approved
 * one-to-one mapping, with no extra foreign-key column and no parent-side
 * association.</p>
 */
@Entity
@Table(name = "post_details")
public class PostDetails {

    // tag::mapsid[]
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Post post;
    // end::mapsid[]

    /**
     * Publication window as a PostgreSQL {@code tsrange}. Ranges have no native
     * Hibernate mapping; {@link PostgreSQLRangeType} is required.
     */
    // tag::pg-types[]
    @Type(PostgreSQLRangeType.class)
    @Column(name = "publication_period", columnDefinition = "tsrange")
    private Range<LocalDateTime> publicationPeriod;

    /**
     * Read-time budget as a full PostgreSQL {@code interval} via Hypersistence
     * Utils.
     */
    @Type(PostgreSQLIntervalType.class)
    @Column(name = "read_time_budget", columnDefinition = "interval")
    private Duration readTimeBudget;

    /**
     * Free-form key/value metadata as a PostgreSQL {@code hstore}. No native
     * Hibernate mapping exists; {@code PostgreSQLHStoreType} is required.
     */
    @Type(io.hypersistence.utils.hibernate.type.basic.PostgreSQLHStoreType.class)
    @Column(name = "attributes", columnDefinition = "hstore")
    private Map<String, String> attributes = new HashMap<>();
    // end::pg-types[]

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

    public Range<LocalDateTime> getPublicationPeriod() {
        return publicationPeriod;
    }

    public void setPublicationPeriod(Range<LocalDateTime> publicationPeriod) {
        this.publicationPeriod = publicationPeriod;
    }

    public Duration getReadTimeBudget() {
        return readTimeBudget;
    }

    public void setReadTimeBudget(Duration readTimeBudget) {
        this.readTimeBudget = readTimeBudget;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
