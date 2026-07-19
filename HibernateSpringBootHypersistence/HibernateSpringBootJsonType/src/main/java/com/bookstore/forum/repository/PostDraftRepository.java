package com.bookstore.forum.repository;

import com.bookstore.forum.entity.PostDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDraftRepository extends JpaRepository<PostDraft, Long> {
}
