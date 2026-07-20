package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * The OLTP mapping of the {@code batch_seq_post} table: one post is inserted per
 * user request, so the identifier comes from a plain {@code SEQUENCE} generator
 * with an {@code allocationSize} of 1.
 *
 * <p>That allocation size is what keeps {@code batch_seq_post_seq} declared as
 * {@code INCREMENT BY 1}. The sequence therefore stays a normal, boring database
 * sequence that <em>anything</em> can consume — an ETL job, a psql script, a
 * trigger — and every {@code nextval} it hands out is actually used, so the
 * identifiers have no gaps.</p>
 *
 * <p>The price is one {@code nextval} roundtrip per inserted row. That is
 * irrelevant for a single-row transaction, but ruinous for a bulk import — which
 * is exactly what {@link BatchedPost} solves, without changing this sequence.</p>
 *
 * @see BatchedPost
 * @see PooledPost
 */
@Entity
@Table(name = "batch_seq_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    @SequenceGenerator(name = "post_sequence", sequenceName = "batch_seq_post_seq", allocationSize = 1)
    private Long id;

    private String title;

    public Post() {
    }

    public Post(String title) {
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
