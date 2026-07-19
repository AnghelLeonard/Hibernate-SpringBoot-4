package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The root forum thread used to contrast the standard Spring Data
 * {@code JpaRepository} with Hypersistence Utils' {@code BaseJpaRepository}.
 *
 * <p>The identifier uses a bare {@code @GeneratedValue} (never
 * {@code GenerationType.IDENTITY}) so that JDBC batching stays enabled &mdash;
 * IDENTITY would force a round-trip per insert and defeat the batched
 * {@code persistAll} this example demonstrates.</p>
 */
@Entity
@Table(name = "base_jpa_post")
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
