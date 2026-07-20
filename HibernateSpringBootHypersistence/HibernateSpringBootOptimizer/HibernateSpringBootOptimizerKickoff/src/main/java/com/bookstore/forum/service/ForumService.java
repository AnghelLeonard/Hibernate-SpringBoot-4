package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.Tag;
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
    public Tag createTag(String name) {
        Tag tag = new Tag(name);
        entityManager.persist(tag);
        return tag;
    }

    @Transactional
    public Post createPost(String title, String createdBy, List<String> tagNames, List<String> reviews) {
        Post post = new Post(title);
        post.addDetails(new PostDetails(createdBy));
        reviews.forEach(review -> post.addComment(new PostComment(review)));
        findTags(tagNames).forEach(post::addTag);

        entityManager.persist(post);
        return post;
    }

    @Transactional(readOnly = true)
    public List<Tag> findTags(List<String> names) {
        return entityManager
            .createQuery("select t from Tag t where t.name in :names", Tag.class)
            .setParameter("names", names)
            .getResultList();
    }

    /**
     * A plain lookup — no join fetch, nothing exotic. It is worth running with
     * the SQL log on: because of the eager mappings, this single {@code find}
     * drags in the details and every comment's post.
     */
    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return entityManager.find(Post.class, id);
    }

    @Transactional(readOnly = true)
    public List<PostComment> findComments() {
        return entityManager
            .createQuery("select c from PostComment c", PostComment.class)
            .getResultList();
    }

    @Transactional
    public void deleteAll() {
        entityManager.createQuery("delete from PostComment").executeUpdate();
        entityManager.createNativeQuery("delete from kickoff_post_tag").executeUpdate();
        entityManager.createQuery("delete from PostDetails").executeUpdate();
        entityManager.createQuery("delete from Post").executeUpdate();
        entityManager.createQuery("delete from Tag").executeUpdate();
    }
}
