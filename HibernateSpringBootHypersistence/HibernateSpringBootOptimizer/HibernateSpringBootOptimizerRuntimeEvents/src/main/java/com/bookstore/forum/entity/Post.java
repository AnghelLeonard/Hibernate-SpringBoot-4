package com.bookstore.forum.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
 * The same aggregate as the previous item, with every reported mapping issue
 * fixed. Read it side by side with the "before" version — the diff is the item.
 *
 * <p>What changed here:</p>
 * <ul>
 *   <li><strong>{@code ManyToManyListEvent}</strong> — {@code tags} is now a
 *       {@link Set}. With a {@code List}, removing one tag makes Hibernate
 *       delete every join-table row for this post and re-insert the survivors;
 *       with a {@code Set} it deletes exactly one row.</li>
 *   <li><strong>{@code OneToOneParentSideEvent}</strong> — {@code details} is
 *       now {@code optional = false} <em>and</em> the child shares this post's
 *       identifier ({@code @MapsId}). Only then can Hibernate know a row exists
 *       without querying for it, so the parent side can be proxied.</li>
 *   <li><strong>{@code BidirectionalSynchronizationEvent}</strong> — both ends
 *       of every bidirectional association are kept in sync. Note the method
 *       names: the Optimizer looks for {@code add}/{@code remove} plus the
 *       <em>entity type</em> name ({@code addPostComment}), not the field
 *       name — {@code addComment} alone does not satisfy it.</li>
 * </ul>
 */
@Entity
@Table(name = "runtime_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "post",
        orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
    private PostDetails details;

    @ManyToMany
    @JoinTable(name = "runtime_post_tag",
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
