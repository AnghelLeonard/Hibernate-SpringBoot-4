package com.bookstore.repository;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.view.AuthorView;

@Repository
@Transactional(readOnly = true)
public interface AuthorBookViewRepository extends EntityViewRepository<AuthorView, Long> {
}
