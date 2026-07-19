package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.PostDraft;
import com.bookstore.forum.entity.PostProperties;
import com.bookstore.forum.repository.PostDraftRepository;
import com.bookstore.forum.repository.PostRepository;
import tools.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class ForumService {

    private final PostRepository postRepository;

    private final PostDraftRepository postDraftRepository;

    public ForumService(PostRepository postRepository, PostDraftRepository postDraftRepository) {
        this.postRepository = postRepository;
        this.postDraftRepository = postDraftRepository;
    }

    @Transactional
    public Post newPost(String title, PostProperties properties) {
        Post post = new Post(title);
        post.setProperties(properties);
        return postRepository.save(post);
    }

    @Transactional
    public PostDraft newDraft(String title, PostProperties properties) {
        PostDraft draft = new PostDraft(title);
        draft.setProperties(properties);
        return postDraftRepository.save(draft);
    }

    @Transactional
    public void replaceDraftProperties(Long draftId, PostProperties properties) {
        postDraftRepository.findById(draftId).orElseThrow()
            .setProperties(properties);
    }

    /**
     * Loads the draft and changes nothing. Used to observe whether the JSON
     * attribute alone makes the entity dirty when the transaction commits.
     */
    @Transactional
    public void touchDraft(Long draftId) {
        postDraftRepository.findById(draftId).orElseThrow();
    }

    /**
     * Loads the post and changes nothing, the {@code JsonType} counterpart
     * of {@link #touchDraft(Long)}.
     */
    @Transactional
    public void touchPost(Long postId) {
        postRepository.findById(postId).orElseThrow();
    }

    @Transactional
    public PostComment addComment(Long postId, String review, JsonNode attributes) {
        Post post = postRepository.findById(postId).orElseThrow();
        PostComment comment = new PostComment(review);
        comment.setAttributes(attributes);
        post.addComment(comment);
        return comment;
    }

    @Transactional
    public void addDetails(Long postId, String createdBy, String metadata, Map<String, String> attributes) {
        Post post = postRepository.findById(postId).orElseThrow();
        PostDetails details = new PostDetails(createdBy);
        details.setMetadata(metadata);
        details.setAttributes(attributes);
        post.setDetails(details);
    }

    @Transactional
    public void replaceProperties(Long postId, PostProperties properties) {
        postRepository.findById(postId).orElseThrow()
            .setProperties(properties);
    }

    @Transactional
    public void updateProperties(Long postId, Consumer<PostProperties> change) {
        change.accept(
            postRepository.findById(postId).orElseThrow()
                .getProperties()
        );
    }

    @Transactional(readOnly = true)
    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public PostDraft findDraftById(Long draftId) {
        return postDraftRepository.findById(draftId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Long> findIdsByFlair(String flair) {
        return postRepository.findIdsByFlair(flair);
    }
}
