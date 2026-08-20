package com.bookstore.forum.service;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostStatus;
import com.bookstore.forum.repository.PostJpaRepository;
import com.bookstore.forum.repository.PostRepository;
import com.bookstore.forum.repository.PostSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * The <strong>wrong</strong> way, kept only to be measured against
 * {@link ForumService}. It extends the recommended service and overrides a
 * single method, {@link #findMostViewedAndApprovedPosts(int)}, with the
 * {@code findAll()} anti-pattern: the entire table is fetched and then filtered,
 * ordered, limited and projected in memory.
 *
 * <p>The method signature is identical to the good one, so a caller cannot tell
 * the two apart &mdash; the full table scan is visible only in this source.
 * It is injected in tests via its bean name, {@code antiPatternForumService}.</p>
 */
@Service
public class AntiPatternForumService extends ForumService {

    public AntiPatternForumService(PostRepository postRepository, PostJpaRepository postJpaRepository) {
        super(postRepository, postJpaRepository);
    }

    // tag::antipattern-findmostviewed[]
    @Override
    @Transactional(readOnly = true)
    public List<PostSummary> findMostViewedAndApprovedPosts(int limit) {
        return postJpaRepository.findAll().stream()                   // SELECT every row
            .filter(post -> post.getStatus() == PostStatus.APPROVED)     // WHERE, in Java
            .sorted(Comparator.comparingLong(Post::getViews).reversed()) // ORDER BY, in Java
            .limit(limit)                                                // LIMIT, in Java
            .map(post -> new PostSummary(post.getTitle(), post.getViews())) // projection, in Java
            .toList();
    }
    // end::antipattern-findmostviewed[]
}
