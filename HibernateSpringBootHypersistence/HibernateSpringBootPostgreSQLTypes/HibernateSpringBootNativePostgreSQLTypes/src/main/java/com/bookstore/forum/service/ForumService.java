package com.bookstore.forum.service;

import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.Tag;
import com.bookstore.forum.repository.PostCommentRepository;
import com.bookstore.forum.repository.PostDetailsRepository;
import com.bookstore.forum.repository.PostRepository;
import com.bookstore.forum.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService {

    private final PostRepository postRepository;
    private final PostDetailsRepository postDetailsRepository;
    private final PostCommentRepository postCommentRepository;
    private final TagRepository tagRepository;

    public ForumService(PostRepository postRepository,
                        PostDetailsRepository postDetailsRepository,
                        PostCommentRepository postCommentRepository,
                        TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.postDetailsRepository = postDetailsRepository;
        this.postCommentRepository = postCommentRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * Persists the details along with its owning {@link com.bookstore.forum.entity.Post}.
     * The root {@code Post} is saved first so that {@code @MapsId} has an
     * identifier to share.
     */
    @Transactional
    public PostDetails savePostWithDetails(PostDetails details) {
        postRepository.save(details.getPost());
        return postDetailsRepository.save(details);
    }

    @Transactional(readOnly = true)
    public PostDetails findDetails(Long id) {
        return postDetailsRepository.findById(id).orElseThrow();
    }

    /**
     * Persists the comment along with its owning {@code Post} (saved first, as
     * the comment references it).
     */
    @Transactional
    public PostComment savePostWithComment(PostComment comment) {
        postRepository.save(comment.getPost());
        return postCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<PostComment> findFromSubnet(String subnet) {
        return postCommentRepository.findFromSubnet(subnet);
    }

    @Transactional
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Optional<Tag> findTagByNameCaseInsensitively(String name) {
        return tagRepository.findByNameCaseInsensitively(name);
    }

    @Transactional
    public void deleteAll() {
        // Children first, then the root Post they reference.
        postDetailsRepository.deleteAllInBatch();
        postCommentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
    }
}
