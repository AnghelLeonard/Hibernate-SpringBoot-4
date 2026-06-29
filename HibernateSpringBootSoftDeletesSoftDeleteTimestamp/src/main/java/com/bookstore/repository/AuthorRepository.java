package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @NativeQuery(value = "SELECT * FROM author")
    List<Author> findAllIncludingDeleted(); 

    @NativeQuery(value = "SELECT * FROM author AS a WHERE a.deleted IS NOT NULL")
    List<Author> findAllOnlyDeleted();

    @Transactional
    @NativeQuery(value = "UPDATE author SET deleted = NULL WHERE id = ?1")
    @Modifying
    public void restoreById(Long id);
}
