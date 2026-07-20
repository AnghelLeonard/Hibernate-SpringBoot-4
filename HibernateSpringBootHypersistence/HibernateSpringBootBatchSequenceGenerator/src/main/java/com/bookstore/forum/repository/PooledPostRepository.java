package com.bookstore.forum.repository;

import com.bookstore.forum.entity.PooledPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PooledPostRepository extends JpaRepository<PooledPost, Long> {
}
