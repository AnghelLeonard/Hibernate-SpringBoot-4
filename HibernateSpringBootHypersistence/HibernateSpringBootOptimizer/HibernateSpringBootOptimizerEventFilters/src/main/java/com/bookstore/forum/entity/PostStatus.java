package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A five-row lookup table: {@code DRAFT}, {@code PUBLISHED}, {@code ARCHIVED}
 * and friends. It exists to justify the one rule this module deliberately
 * breaks — see {@link Post#getStatus()}.
 */
@Entity
@Table(name = "filtered_post_status")
public class PostStatus {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public PostStatus() {
    }

    public PostStatus(String name) {
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
