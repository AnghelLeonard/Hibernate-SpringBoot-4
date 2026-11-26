package com.bookstore.repository;

import com.bookstore.entity.AuthorSet;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorSetRepository extends BaseJpaRepository<AuthorSet, Long> {
}
