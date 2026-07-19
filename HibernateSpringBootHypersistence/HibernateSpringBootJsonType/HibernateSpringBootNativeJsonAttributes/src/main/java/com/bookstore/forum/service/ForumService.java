package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
public class ForumService {

    private final PostRepository postRepository;

    public ForumService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void update(Long id, Consumer<Post> change) {
        change.accept(postRepository.findById(id).orElseThrow());
    }

    /**
     * Loads the post and changes nothing, to observe whether a JSON attribute
     * alone makes the entity dirty when the transaction commits.
     */
    @Transactional
    public void touch(Long id) {
        postRepository.findById(id).orElseThrow();
    }
}
