package com.bookstore.repository;

import com.bookstore.entity.Author;
import com.bookstore.entity.AuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, AuthorId> {

    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE a.publisher.urc = ?1 AND a.id.name = ?2")
    Author fetchWithBooks(int urc, String name);

    @Query("SELECT a FROM Author a WHERE a.id.name = ?1")
    public Author fetchByName(String name);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Author a WHERE a.publisher.urc = ?1 AND a.id.name = ?2")
    int deleteByPublisherUrcAndName(int urc, String name);
}
