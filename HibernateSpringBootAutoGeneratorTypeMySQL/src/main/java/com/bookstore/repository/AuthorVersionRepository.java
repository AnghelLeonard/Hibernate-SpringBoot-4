package com.bookstore.repository;

import com.bookstore.entity.AuthorVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorVersionRepository extends JpaRepository<AuthorVersion, Long> {    
}
