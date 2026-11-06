package com.bookstore.repository;

import com.bookstore.entity.AuthorPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorPersistableRepository extends JpaRepository<AuthorPersistable, Long> {    
}
