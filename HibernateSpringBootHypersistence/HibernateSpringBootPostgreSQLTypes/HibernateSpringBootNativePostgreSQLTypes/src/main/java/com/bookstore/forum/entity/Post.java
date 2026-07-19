package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The root forum thread. As in the Hypersistence Utils module, every other
 * entity hangs off a {@code Post}: {@link PostDetails} shares its identifier via
 * {@code @OneToOne @MapsId} and {@link PostComment} references it via
 * {@code @ManyToOne}. The root is always mapped first.
 */
@Entity
@Table(name = "native_post")
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
