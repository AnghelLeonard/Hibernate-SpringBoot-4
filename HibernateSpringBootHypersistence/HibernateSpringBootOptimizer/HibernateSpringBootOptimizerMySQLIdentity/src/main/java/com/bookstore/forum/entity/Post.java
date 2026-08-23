package com.bookstore.forum.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The same fixed aggregate as the previous item, but mapped for MySQL. Because
 * MySQL has no sequences, the identifier uses {@code GenerationType.IDENTITY},
 * which the Optimizer still reports as an {@code IdentityGeneratorEvent} — the
 * one issue this module chooses to waive rather than fix.
 */
@Entity
@Table(name = "identity_post")
public class Post {

    // tag::identity[]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // end::identity[]

    private String title;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "post",
        orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
    private PostDetails details;

    @ManyToMany
    @JoinTable(name = "identity_post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

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

    public PostDetails getDetails() {
        return details;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void addPostComment(PostComment postComment) {
        comments.add(postComment);
        postComment.setPost(this);
    }

    public void removePostComment(PostComment postComment) {
        comments.remove(postComment);
        postComment.setPost(null);
    }

    public void addPostDetails(PostDetails postDetails) {
        this.details = postDetails;
        postDetails.setPost(this);
    }

    public void removePostDetails(PostDetails postDetails) {
        this.details = null;
        postDetails.setPost(null);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
}
