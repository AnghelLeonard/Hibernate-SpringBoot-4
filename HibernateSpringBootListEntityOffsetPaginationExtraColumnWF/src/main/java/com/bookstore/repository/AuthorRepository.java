package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {

    @NativeQuery(value = """
                         SELECT id, name, age, genre, COUNT(*) OVER() AS total
                         FROM author ORDER BY age LIMIT ?1, ?2
                         """)
    List<Author> fetchAll(int page, int size);        
}
