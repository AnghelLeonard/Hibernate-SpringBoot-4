package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {   

    @Query(value = "SELECT a FROM Author a ORDER BY a.age LIMIT ?1 OFFSET ?2")
    List<Author> fetchAllJpql(int size, int offset);
    
    // ANSI-SQL OFFSET / FETCH NEXT Syntax
    @Query(value = "SELECT a FROM Author a ORDER BY a.age OFFSET ?1 ROWS FETCH NEXT ?2 ROWS ONLY")
    List<Author> fetchAllJpqlAnsi(int offset, int limit);
    
    @NativeQuery(value = """
                         SELECT id, name, age, genre, COUNT(*) OVER() AS total
                         FROM author ORDER BY age LIMIT ?1, ?2
                         """)
    List<Author> fetchAllNative(int page, int size);
}
