package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> { 

    @Query(value = "SELECT a FROM Author a WHERE a.deleted IS NOT NULL")
    List<Author> findAllOnlyDeleted();

    @Transactional
    @Query(value = "UPDATE Author a SET a.deleted = NULL WHERE a.id = ?1")
    @Modifying
    public void restoreById(Long id);
}
