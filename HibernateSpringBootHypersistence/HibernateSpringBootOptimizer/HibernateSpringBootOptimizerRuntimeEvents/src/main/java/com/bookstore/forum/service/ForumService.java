package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.PostSummary;
import com.bookstore.forum.entity.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
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
        post.addPostDetails(new PostDetails(createdBy));
        reviews.forEach(review -> post.addPostComment(new PostComment(review)));
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
     * With the fixed mappings this loads the post row and nothing else — the
     * details and the comments' parents are all proxies now.
     */
    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return entityManager.find(Post.class, id);
    }

    /**
     * Fetches every comment at once. With no upper bound, a query like this
     * reports a {@code QueryResultListSizeEvent} as soon as the result set grows
     * past {@code hypersistence.query.max_result_size}.
     */
    // tag::result-list[]
    @Transactional(readOnly = true)
    public List<PostComment> findComments() {
        return entityManager
            .createQuery("select c from PostComment c", PostComment.class)
            .getResultList();
    }
    // end::result-list[]

    /**
     * Page 2 of <em>what</em>? Without an {@code order by}, the database is free
     * to return rows in any order it likes, so two requests for the same page
     * can return different rows — and neither the mapping nor the configuration
     * can warn you about it. Only watching the query run can.
     *
     * @see #findPageOrdered(int, int)
     */
    // tag::pagination[]
    @Transactional(readOnly = true)
    public List<Post> findPage(int pageNumber, int pageSize) {
        return entityManager
            .createQuery("select p from Post p", Post.class)
            .setFirstResult(pageNumber * pageSize)
            .setMaxResults(pageSize)
            .getResultList();
    }
    // end::pagination[]

    /**
     * The same page, made deterministic.
     */
    @Transactional(readOnly = true)
    public List<Post> findPageOrdered(int pageNumber, int pageSize) {
        return entityManager
            .createQuery("select p from Post p order by p.id", Post.class)
            .setFirstResult(pageNumber * pageSize)
            .setMaxResults(pageSize)
            .getResultList();
    }

    /**
     * The mapping is correct — {@code PostComment.post} is {@code LAZY}, exactly
     * as the previous item made it. The <em>code</em> is what is wrong: touching
     * the post of every comment initializes one proxy per row. This is the N+1
     * that static analysis can never catch, because it lives in the loop below,
     * not in an annotation.
     */
    // tag::n-plus-one[]
    @Transactional(readOnly = true)
    public List<String> findCommentAuthorsOneByOne() {
        return findComments().stream()
            .map(comment -> comment.getPost().getTitle())
            .toList();
    }
    // end::n-plus-one[]

    /**
     * The same information, in one query.
     */
    // tag::join-fetch[]
    @Transactional(readOnly = true)
    public List<String> findCommentAuthorsInOneQuery() {
        return entityManager.createQuery("""
                select c
                from PostComment c
                join fetch c.post
                """, PostComment.class)
            .getResultList().stream()
            .map(comment -> comment.getPost().getTitle())
            .toList();
    }
    // end::join-fetch[]

    /**
     * A single query that runs longer than {@code hypersistence.query.timeout_millis},
     * so the runtime scanner reports a {@code QueryTimeoutEvent}.
     */
    // tag::slow-query[]
    @Transactional(readOnly = true)
    public void runSlowQuery() {
        entityManager.createNativeQuery("select pg_sleep(0.2)").getSingleResult();
    }
    // end::slow-query[]

    /**
     * The {@code find} call already puts the {@code Post} into the persistence
     * context, so the following {@code merge} is redundant and the runtime
     * scanner reports an {@code EntityAlreadyManagedEvent}.
     */
    // tag::already-managed[]
    @Transactional
    public void mergeAnAlreadyManagedEntity(Long postId) {
        Post post = entityManager.find(Post.class, postId);
        entityManager.merge(post);
    }
    // end::already-managed[]

    /**
     * Loads the same table row through two different entities, so the runtime
     * scanner reports a {@code TableRowAlreadyManagedEvent}.
     */
    // tag::table-row-load[]
    @Transactional(readOnly = true)
    public void loadSameRowAsTwoEntities(Long postId) {
        entityManager.find(Post.class, postId);
        entityManager.find(PostSummary.class, postId);
    }
    // end::table-row-load[]

    /**
     * Persists a thousand entities in one transaction, with JDBC batching turned
     * off for this session so Hibernate sends one INSERT per entity. When the
     * transaction commits, that single flush takes longer than
     * {@code hypersistence.session.flush_timeout_millis}, so the runtime scanner
     * reports a {@code SessionFlushTimeoutEvent}.
     */
    // tag::slow-flush[]
    @Transactional
    public void flushManyEntities() {
        entityManager.unwrap(Session.class).setJdbcBatchSize(1);

        for (int i = 0; i < 1_000; i++) {
            entityManager.persist(new Tag("flush-tag-" + i));
        }
    }
    // end::slow-flush[]

    /**
     * A transaction that holds its connection far longer than the work needs —
     * the shape of every "we only added a REST call inside the service method"
     * incident. The connection pool is the scarcest resource in the system.
     */
    // tag::session-timeout[]
    @Transactional
    public void slowTransaction(long millis) {
        entityManager
            .createQuery("select count(p) from Post p", Long.class)
            .getSingleResult();
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    // end::session-timeout[]

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
        entityManager.createNativeQuery("delete from runtime_post_tag").executeUpdate();
        entityManager.createQuery("delete from PostComment").executeUpdate();
        entityManager.createQuery("delete from PostDetails").executeUpdate();
        entityManager.createQuery("delete from Post").executeUpdate();
        entityManager.createQuery("delete from Tag").executeUpdate();
    }
}
