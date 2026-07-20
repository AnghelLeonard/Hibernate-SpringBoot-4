package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.id.SequenceOptimizer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The counter-example: the classic way to cut down sequence roundtrips is the
 * {@code pooled} optimizer, which reserves a whole block of identifiers per call.
 * {@code @SequenceOptimizer} is the Hypersistence Utils annotation that lets you
 * pick the optimizer explicitly instead of relying on Hibernate's implicit
 * mapping of {@code allocationSize} onto an optimizer.
 *
 * <p>It works, but it is <em>not</em> free:</p>
 * <ul>
 *   <li>The sequence must be declared {@code INCREMENT BY 50}. A non-Hibernate
 *       client that calls {@code nextval} gets a single identifier and burns the
 *       other 49 — it cannot benefit from the pooling, because the pooling lives
 *       in Hibernate, not in the database.</li>
 *   <li>The reserved block is cached per {@code SessionFactory}. Two application
 *       instances (or two {@code EntityManagerFactory} objects) each hold their
 *       own block, so the identifiers they assign are interleaved in wide,
 *       permanent gaps — see the two-EMF test.</li>
 *   <li>Anything not consumed when the application shuts down is lost forever.</li>
 * </ul>
 *
 * <p>{@link BatchedPost} gets the same roundtrip reduction with none of this,
 * because it batches the <em>fetch</em> rather than inflating the increment.</p>
 *
 * @see Post
 * @see BatchedPost
 */
@Entity
@Table(name = "pooled_post")
public class PooledPost {

    /**
     * The {@code pooled} optimizer reads each {@code nextval} as the <em>upper</em>
     * bound of the block it just reserved, so one call to a sequence declared
     * {@code INCREMENT BY 50} buys the identifiers
     * {@code [nextval - 49, nextval]}.
     */
    @Id
    @SequenceOptimizer(sequenceName = "pooled_post_seq", incrementSize = 50, optimizer = "pooled")
    private Long id;

    private String title;

    public PooledPost() {
    }

    public PooledPost(String title) {
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
