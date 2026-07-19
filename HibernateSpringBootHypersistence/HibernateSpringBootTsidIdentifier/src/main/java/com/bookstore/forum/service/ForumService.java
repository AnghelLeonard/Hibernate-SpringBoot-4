package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.Tag;
import com.bookstore.forum.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ForumService {

    private final PostRepository postRepository;

    public ForumService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post newPost(String title, List<String> tagNames) {
        Post post = new Post(title);

        post.addComment(new PostComment("Excellent read!"));
        post.addComment(new PostComment("Best JPA book so far."));

        post.setDetails(new PostDetails("vlad"));

        tagNames.forEach(tagName -> post.addTag(new Tag(tagName)));

        return postRepository.save(post);
    }

    @Transactional
    public List<Post> insertPosts(int postCount) {
        List<Post> posts = new ArrayList<>();
        IntStream.rangeClosed(1, postCount).forEach(i ->
            posts.add(new Post(String.format("Post nr. %d", i)))
        );
        return postRepository.saveAll(posts);
    }

    @Transactional(readOnly = true)
    public List<Post> findByTitle(String title) {
        return postRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public Post findByIdWithComments(Long id) {
        return postRepository.findByIdWithComments(id);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public long countPosts() {
        return postRepository.count();
    }
}
