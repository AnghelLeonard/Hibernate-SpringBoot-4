package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The root forum thread. {@link PostComment} references it, giving the query
 * utilities a real join to work on.
 */
@Entity
@Table(name = "query_util_post")
public class Post {

    @Id
    @GeneratedValue
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
