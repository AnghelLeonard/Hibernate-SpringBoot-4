package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.repository.PostCommentRepository;
import com.bookstore.forum.repository.PostRepository;
import io.hypersistence.utils.hibernate.query.SQLExtractor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ForumService {

    public static final String COMMENTS_WITH_POST_JPQL =
        "select c from PostComment c join fetch c.post p where p.title = :title";

    @PersistenceContext
    private EntityManager entityManager;

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    public ForumService(PostRepository postRepository, PostCommentRepository postCommentRepository) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
    }

    @Transactional
    public Long createPostWithComments(String title, String... reviews) {
        Post post = postRepository.save(new Post(title));
        for (String review : reviews) {
            postCommentRepository.save(new PostComment(post, review));
        }
        return post.getId();
    }

    /**
     * Executes a JPQL query with a join. Running it triggers the registered
     * {@code StatementInspector}s, so the {@code QueryStackTraceLogger} logs the
     * originating {@code com.bookstore.forum} frame and the {@code JfrQueryLogger}
     * records a JFR event.
     */
    @Transactional(readOnly = true)
    public List<PostComment> findCommentsWithPost(String title) {
        return entityManager
            .createQuery(COMMENTS_WITH_POST_JPQL, PostComment.class)
            .setParameter("title", title)
            .getResultList();
    }

    @Transactional
    public void deleteAll() {
        postCommentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
    }
}
