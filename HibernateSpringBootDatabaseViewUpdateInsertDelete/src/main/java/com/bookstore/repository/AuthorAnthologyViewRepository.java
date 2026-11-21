package com.bookstore.repository;

import com.bookstore.view.AuthorAnthologyView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorAnthologyViewRepository extends JpaRepository<AuthorAnthologyView, Long> {
    
    public AuthorAnthologyView findByName(String name);
}
