package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.dto.AuthorNameAge;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Scalar Mapping
    List<String> fetchName(Sort sort, Limit limit);
  
    // Spring projection
    List<AuthorNameAge> fetchNameAndAge(Sort sort, Limit limit);
}
