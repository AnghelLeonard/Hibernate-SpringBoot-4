package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteAll() {
        postRepository.deleteAllInBatch();
    }
}
