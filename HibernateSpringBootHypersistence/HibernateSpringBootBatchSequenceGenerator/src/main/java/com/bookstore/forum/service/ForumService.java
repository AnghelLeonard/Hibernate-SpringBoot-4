package com.bookstore.forum.service;

import com.bookstore.forum.entity.BatchedPost;
import com.bookstore.forum.entity.PooledPost;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.repository.PooledPostRepository;
import com.bookstore.forum.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ForumService {

    private final PostRepository postRepository;
    private final PooledPostRepository pooledPostRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ForumService(PostRepository postRepository,
                        PooledPostRepository pooledPostRepository) {
        this.postRepository = postRepository;
        this.pooledPostRepository = pooledPostRepository;
    }

    /**
     * The OLTP path: one post, one {@code nextval}, no gap.
     */
    @Transactional
    public Post createPost(String title) {
        Post post = new Post(title);
        entityManager.persist(post);
        entityManager.flush();
        return post;
    }

    /**
     * The import path: {@code fetchSize} identifiers per sequence roundtrip and
     * a single batched insert, against the same sequence as {@link #createPost}.
     */
    @Transactional
    public List<BatchedPost> importPosts(List<String> titles) {
        List<BatchedPost> posts = titles.stream().map(BatchedPost::new).toList();
        posts.forEach(entityManager::persist);
        entityManager.flush();
        return posts;
    }

    @Transactional
    public PooledPost createPooledPost(String title) {
        PooledPost post = new PooledPost(title);
        entityManager.persist(post);
        entityManager.flush();
        return post;
    }

    /**
     * What a database script, an ETL job or a stored procedure would do: ask the
     * sequence for a value and insert a row with it, knowing nothing about
     * Hibernate. It works against {@code batch_seq_post_seq} precisely because
     * that sequence is still {@code INCREMENT BY 1} and nobody is holding a
     * reserved block of it in memory.
     */
    @Transactional
    public Long insertPostAsADatabaseScriptWould(String title) {
        Long id = ((Number) entityManager
            .createNativeQuery("select nextval('batch_seq_post_seq')")
            .getSingleResult()).longValue();

        entityManager
            .createNativeQuery("insert into batch_seq_post (id, title) values (:id, :title)")
            .setParameter("id", id)
            .setParameter("title", title)
            .executeUpdate();

        return id;
    }

    @Transactional(readOnly = true)
    public List<Post> findAllPostsOrderedById() {
        return entityManager
            .createQuery("select p from Post p order by p.id", Post.class)
            .getResultList();
    }

    /**
     * {@link Post} and {@link BatchedPost} share the {@code batch_seq_post}
     * table, so wiping the posts wipes the imported ones as well.
     */
    @Transactional
    public void deleteAll() {
        postRepository.deleteAllInBatch();
        pooledPostRepository.deleteAllInBatch();
    }
}
