package com.bookstore.forum.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * A deliberately small aggregate — a {@code Post} and its {@code PostComment}
 * list. The mapping itself is clean; what we want from this item is not another
 * mapping issue, but a steady stream of <em>runtime</em> events (a large result
 * list and an N+1 query) that the Event Persistence feature can store and the
 * web app can display.
 */
@Entity
@Table(name = "forum_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

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

    public List<PostComment> getComments() {
        return comments;
    }

    public void addPostComment(PostComment postComment) {
        comments.add(postComment);
        postComment.setPost(this);
    }

    public void removePostComment(PostComment postComment) {
        comments.remove(postComment);
        postComment.setPost(null);
    }
}
