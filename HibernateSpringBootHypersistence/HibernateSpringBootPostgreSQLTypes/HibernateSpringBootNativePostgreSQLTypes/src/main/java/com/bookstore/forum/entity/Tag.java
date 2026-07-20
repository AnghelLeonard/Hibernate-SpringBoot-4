package com.bookstore.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A tag whose {@code name} is stored in a PostgreSQL {@code citext} column
 * without any Hypersistence Utils type: a plain {@link String} field with
 * {@code columnDefinition = "citext"} is enough to make the DDL create a
 * case-insensitive column.
 *
 * <p><strong>But the native mapping is only half the story.</strong> Because the
 * case-insensitivity lives in the column type, the <em>unique constraint</em> is
 * enforced case-insensitively ({@code "Hibernate"} and {@code "hibernate"}
 * collide). However, Hibernate has no native {@code citext} type, so it binds
 * query parameters as {@code varchar} &mdash; which means a derived-query
 * equality lookup ({@code name = ?}) is compared case-<em>sensitively</em> by
 * PostgreSQL. To get a case-insensitive lookup with the native mapping you must
 * cast the parameter explicitly ({@code name = CAST(? AS citext)}). The
 * Hypersistence Utils {@code PostgreSQLCITextType} avoids this by binding the
 * parameter as {@code citext}, so <em>its</em> lookups are case-insensitive with
 * no extra casting.</p>
 */
@Entity
@Table(name = "native_tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", columnDefinition = "citext", unique = true)
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
