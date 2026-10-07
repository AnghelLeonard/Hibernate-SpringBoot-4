package com.bookstore.repository;

import com.bookstore.entity.Author;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    Window<Author> findBy(ScrollPosition scrollPosition, Sort sort, Limit limit);
    
    // Window<AuthorDto> findBy(ScrollPosition scrollPosition, Sort sort, Limit limit);
    
    // use any derived query method and pass the proper parameters
}
