package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLCITextType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

/**
 * A tag whose {@code name} is stored in a PostgreSQL {@code citext}
 * (case-insensitive text) column via {@link PostgreSQLCITextType}. The unique
 * constraint on a {@code citext} column is enforced case-insensitively, so
 * {@code "Hibernate"} and {@code "hibernate"} collide, and equality lookups
 * match regardless of case &mdash; behaviour Hibernate cannot express through a
 * plain {@code varchar} mapping.
 */
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @Type(PostgreSQLCITextType.class)
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
