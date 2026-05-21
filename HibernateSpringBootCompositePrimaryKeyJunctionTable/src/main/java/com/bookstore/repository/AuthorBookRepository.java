package com.bookstore.repository;

import com.bookstore.entity.AuthorBook;
import com.bookstore.entity.AuthorBookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorBookRepository extends JpaRepository<AuthorBook, AuthorBookId> {
}
