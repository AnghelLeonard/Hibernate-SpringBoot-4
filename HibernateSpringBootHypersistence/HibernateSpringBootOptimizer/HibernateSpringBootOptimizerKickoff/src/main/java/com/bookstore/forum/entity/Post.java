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
import java.util.List;

/**
 * <strong>This model is deliberately suboptimal.</strong> It is the "before"
 * picture the chapter opens with: every mapping below is something you are
 * likely to find in a real code base, and every one of them is reported by
 * Hypersistence Optimizer at startup. The next item fixes them one at a time.
 *
 * <p>What is wrong here:</p>
 * <ul>
 *   <li>{@code details} is the <em>parent</em> side of a {@code @OneToOne}
 *       ({@code mappedBy}), which Hibernate cannot proxy — it must run an extra
 *       query to decide whether to store {@code null} → {@code OneToOneParentSideEvent}.</li>
 *   <li>{@code tags} is a {@code @ManyToMany} over a {@code List}: removing one
 *       tag deletes the whole join-table content for this post and re-inserts
 *       the rest → {@code ManyToManyListEvent}.</li>
 * </ul>
 *
 * @see PostDetails
 * @see PostComment
 */
@Entity
@Table(name = "kickoff_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "post",
        orphanRemoval = true, fetch = FetchType.LAZY)
    private PostDetails details;

    @ManyToMany
    @JoinTable(name = "kickoff_post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

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

    public List<Tag> getTags() {
        return tags;
    }

    public void addComment(PostComment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void addDetails(PostDetails postDetails) {
        this.details = postDetails;
        postDetails.setPost(this);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }
}
