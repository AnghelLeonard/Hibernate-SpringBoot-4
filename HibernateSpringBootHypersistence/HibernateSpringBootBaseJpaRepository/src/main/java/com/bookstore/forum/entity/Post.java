package com.bookstore.forum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 *
 * <p>The {@code status} and {@code views} columns exist for the {@code findAll()}
 * anti-pattern example, where filtering, ordering and limiting belong in SQL
 * rather than in an in-memory {@code Stream}.</p>
 */
@Entity
@Table(name = "base_jpa_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PostStatus status;

    private long views;

    public Post() {
    }

    public Post(String title) {
        this.title = title;
    }

    public Post(String title, PostStatus status, long views) {
        this.title = title;
        this.status = status;
        this.views = views;
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

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
