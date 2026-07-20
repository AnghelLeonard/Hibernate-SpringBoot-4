package com.bookstore.forum.repository;

import com.bookstore.forum.entity.BatchedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchedPostRepository extends JpaRepository<BatchedPost, Long> {
}
