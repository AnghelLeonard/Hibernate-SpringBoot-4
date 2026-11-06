package com.bookstore.repository;

import com.bookstore.entity.AuthorIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorIdentityRepository extends JpaRepository<AuthorIdentity, Long> {    
}
