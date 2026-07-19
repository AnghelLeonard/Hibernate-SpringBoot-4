package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A forum thread with a {@code likes} counter that two transactions compete to
 * update under a pessimistic lock.
 *
 * <p>Deliberately <strong>no {@code @Version}</strong>: this example retries a
 * <em>recoverable</em> failure (a lock-acquisition timeout), not an
 * optimistic-locking conflict. An {@code OptimisticLockException} means someone
 * else already committed a change your write was based on &mdash; blindly
 * retrying would overwrite their work, so it is not retried here.</p>
 */
@Entity
@Table(name = "retry_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private int likes;

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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
