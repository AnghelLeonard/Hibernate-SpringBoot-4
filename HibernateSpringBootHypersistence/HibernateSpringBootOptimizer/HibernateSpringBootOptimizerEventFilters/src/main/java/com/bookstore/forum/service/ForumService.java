package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.PostStatus;
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
        post.setStatus(findStatus("DRAFT"));
        post.addPostDetails(new PostDetails(createdBy));
        reviews.forEach(review -> post.addPostComment(new PostComment(review)));
        findTags(tagNames).forEach(post::addTag);

        entityManager.persist(post);
        return post;
    }

    /**
     * The lookup rows are seeded by the schema script, so this is a read.
     */
    @Transactional(readOnly = true)
    public PostStatus findStatus(String name) {
        return entityManager
            .createQuery("select s from PostStatus s where s.name = :name", PostStatus.class)
            .setParameter("name", name)
            .getSingleResult();
    }

    @Transactional(readOnly = true)
    public List<Tag> findTags(List<String> names) {
        return entityManager
            .createQuery("select t from Tag t where t.name in :names", Tag.class)
            .setParameter("names", names)
            .getResultList();
    }

    /**
     * With the fixed mappings this loads the post row and nothing else — the
     * details and the comments' parents are all proxies now.
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

    /**
     * Removing a single tag now deletes a single join-table row, because
     * {@code tags} is a {@code Set}.
     */
    @Transactional
    public void removeTag(Long postId, String tagName) {
        Post post = entityManager.find(Post.class, postId);
        post.getTags().removeIf(tag -> tag.getName().equals(tagName));
    }

    @Transactional
    public void deleteAll() {
        entityManager.createNativeQuery("delete from filtered_post_tag").executeUpdate();
        entityManager.createQuery("delete from PostComment").executeUpdate();
        entityManager.createQuery("delete from PostDetails").executeUpdate();
        entityManager.createQuery("delete from Post").executeUpdate();
        entityManager.createQuery("delete from Tag").executeUpdate();
    }
}
