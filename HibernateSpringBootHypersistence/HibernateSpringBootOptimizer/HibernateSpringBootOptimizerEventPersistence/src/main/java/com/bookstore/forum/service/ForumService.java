package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ForumService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Post createPost(String title, List<String> reviews) {
        Post post = new Post(title);
        reviews.forEach(review -> post.addPostComment(new PostComment(review)));
        entityManager.persist(post);
        return post;
    }

    /**
     * Fetches every comment at once. With no upper bound, once the result set
     * grows past {@code hypersistence.query.max_result_size}, the runtime scanner
     * reports a {@code QueryResultListSizeEvent}, which the Event Persistence
     * feature rolls up and stores.
     */
    @Transactional(readOnly = true)
    public List<PostComment> findComments() {
        return entityManager
            .createQuery("select c from PostComment c", PostComment.class)
            .getResultList();
    }

    /**
     * The mapping is correct — {@code PostComment.post} is {@code LAZY}. The
     * <em>code</em> is what is wrong: touching the post of every comment
     * initializes one proxy per row. This is the N+1 that lives in the loop
     * below, not in an annotation, so only the runtime scanner can catch it.
     */
    @Transactional(readOnly = true)
    public List<String> findCommentAuthorsOneByOne() {
        return findComments().stream()
            .map(comment -> comment.getPost().getTitle())
            .toList();
    }

    @Transactional
    public void deleteAll() {
        entityManager.createQuery("delete from PostComment").executeUpdate();
        entityManager.createQuery("delete from Post").executeUpdate();
    }
}
