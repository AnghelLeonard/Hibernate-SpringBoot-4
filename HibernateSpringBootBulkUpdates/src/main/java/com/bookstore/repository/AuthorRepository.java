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
    
    @Query("SELECT DISTINCT a FROM Author a JOIN FETCH a.books b WHERE a.age > ?1")
    List<Author> findGtGivenAge(int age);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Author a SET a.age = a.age + 1, a.version = a.version + 1")
    public int updateInBulk();

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Author a SET a.age = a.age + 1, a.version = a.version + 1 WHERE a IN ?1")
    public int updateInBulk(List<Author> authors);
}
