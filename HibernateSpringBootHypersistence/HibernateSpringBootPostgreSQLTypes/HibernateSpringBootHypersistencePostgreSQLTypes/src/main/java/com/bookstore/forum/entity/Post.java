package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The root forum thread. Every other entity in this module hangs off a
 * {@code Post}: {@link PostDetails} shares its identifier through
 * {@code @OneToOne @MapsId}, and {@link PostComment} references it through
 * {@code @ManyToOne}. A details/child row cannot exist without its owning
 * {@code Post}, so the root is always mapped first.
 */
@Entity
@Table(name = "post")
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
