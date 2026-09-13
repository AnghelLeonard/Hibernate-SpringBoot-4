package com.bookstore.repository;

import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Modifying
    @NativeQuery(value = "INSERT INTO author (id, name) VALUES (NEXTVAL('custom_pooled_lo_sequence'), ?1)")
    public void saveNative(String name);
}
