package com.bookstore.repository;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = """
                   SELECT a FROM Author a 
                   WHERE (:lid IS NULL OR a.id < :lid) 
                   ORDER BY a.id DESC LIMIT :limit
                   """)
    List<Author> fetchAll(@Param("lid") long lid, @Param("limit") int limit);
    
    @Query(value = """
                   SELECT a FROM Author a 
                   WHERE (:lid IS NULL OR a.id < :lid) 
                   ORDER BY a.id DESC
                   """)
    List<Author> fetchAllPageable(@Param("lid") long lid, Pageable pagrable);

    @Query(value = """
                   SELECT a.name AS name, a.age AS age FROM Author a 
                   WHERE (:lid IS NULL OR a.id < :lid) 
                   ORDER BY a.id DESC LIMIT :limit
                   """)
    List<AuthorDto> fetchAllDto(@Param("lid") long lid, @Param("limit") int limit);
}
