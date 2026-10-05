package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly= true)
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {        
    
    @Query(value = "SELECT a FROM Author a")
    List<Author> fetchAllJpql(Pageable pageable);   
    
    @NativeQuery(value = "SELECT id, name, age, genre, COUNT(*) OVER() AS total FROM author")
    List<Author> fetchAllNative(Pageable pageable);      
}
