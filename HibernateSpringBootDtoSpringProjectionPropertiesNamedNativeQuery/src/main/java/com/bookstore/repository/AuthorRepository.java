package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.dto.AuthorNameAge;
import org.springframework.data.jpa.repository.NativeQuery;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Scalar Mapping
    @NativeQuery
    List<String> fetchName();
  
    // Spring projection
    @NativeQuery
    List<AuthorNameAge> fetchNameAndAge();
}
