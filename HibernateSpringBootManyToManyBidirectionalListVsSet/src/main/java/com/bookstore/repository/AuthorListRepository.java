package com.bookstore.repository;

import com.bookstore.entity.AuthorList;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorListRepository extends BaseJpaRepository<AuthorList, Long> { 
}
