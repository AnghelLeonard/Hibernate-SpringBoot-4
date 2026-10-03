package com.bookstore.repository;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = """
                   SELECT a FROM Author a 
                   WHERE :ltime IS NULL OR (a.createdAt, a.id) < (:ltime, :lid)
                   ORDER BY a.createdAt DESC, a.id DESC LIMIT :size
                   """)
    List<AuthorDto> fetchNextPage(
            @Param("ltime") Instant ltime, @Param("lid") Long lid, @Param("size") int size);
    
    // you can also add LIMIT via Pageable
    @Query(value = """
                   SELECT a FROM Author a 
                   WHERE :ltime IS NULL OR (a.createdAt, a.id) < (:ltime, :lid)
                   ORDER BY a.createdAt DESC, a.id DESC
                   """)
    List<AuthorDto> fetchNextPageable(
            @Param("ltime") Instant ltime, @Param("lid") Long lid, Pageable pageable);    
}
