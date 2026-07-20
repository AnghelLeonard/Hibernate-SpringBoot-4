package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.id.BatchSequence;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The bulk-import mapping of the very same {@code batch_seq_post} table, drawing
 * from the very same {@code batch_seq_post_seq} sequence as {@link Post} — only
 * the identifier <em>fetching strategy</em> differs.
 *
 * <p>{@code @BatchSequence} asks for {@code fetchSize} values in a single
 * roundtrip using a recursive CTE (on PostgreSQL, a {@code generate_series}
 * variant):</p>
 *
 * <pre>{@code
 * select nextval('batch_seq_post_seq') from generate_series(1, ?)
 * }</pre>
 *
 * <p>Every value returned is a real {@code nextval}, so the sequence keeps its
 * {@code INCREMENT BY 1} definition — the roundtrips are batched, not the
 * sequence itself. That is the whole point: importing {@code fetchSize} rows
 * costs <em>one</em> sequence call plus one batched insert, and yet the OLTP
 * mapping above and any non-Hibernate client can go on calling {@code nextval}
 * against the same sequence, interleaved, with no coordination.</p>
 *
 * <p>Only the unused tail of a fetch is lost, so size {@code fetchSize} after
 * your import chunk. Contrast that with the {@code pooled} optimizer, which
 * redefines the sequence's increment <em>permanently, for every client</em>.</p>
 *
 * @see Post
 * @see PooledPost
 */
@Entity
@Table(name = "batch_seq_post")
public class BatchedPost {

    // tag::batch-sequence[]
    @Id
    @BatchSequence(name = "batch_seq_post_seq", fetchSize = 10)
    private Long id;
    // end::batch-sequence[]

    private String title;

    public BatchedPost() {
    }

    public BatchedPost(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
