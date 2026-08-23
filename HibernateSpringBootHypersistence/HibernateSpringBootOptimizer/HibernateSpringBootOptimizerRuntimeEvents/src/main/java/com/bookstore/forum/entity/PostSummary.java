package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A second entity mapped to the very same {@code runtime_post} table as
 * {@link Post}. Loading the same row through both entities in one
 * {@code Session} makes the runtime scanner report a
 * {@code TableRowAlreadyManagedEvent}.
 */
// tag::table-row[]
@Entity
@Table(name = "runtime_post")
public class PostSummary {

    @Id
    private Long id;

    private String title;

    // end::table-row[]
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    // tag::table-row[]
}
// end::table-row[]
