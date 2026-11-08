package com.bookstore.repository;

import com.bookstore.entity.AuthorBook;
import com.bookstore.entity.AuthorBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorBookRepository extends JpaRepository<AuthorBook, AuthorBookId> {
}
