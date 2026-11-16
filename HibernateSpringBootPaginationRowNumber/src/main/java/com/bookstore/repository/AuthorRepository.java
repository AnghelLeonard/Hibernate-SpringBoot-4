package com.bookstore.repository;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = """
                   SELECT b.name AS name, b.age AS age, b.total AS total 
                        FROM (
                            SELECT a.name AS name, 
                                   a.age AS age, 
                                   COUNT(*) OVER() AS total,
                                   ROW_NUMBER() OVER (ORDER BY a.age ASC, a.id ASC) AS row_num 
                            FROM Author a
                        ) b 
                        WHERE b.row_num BETWEEN ?1 AND ?2
                        ORDER BY b.row_num ASC
                         """)
    List<AuthorDto> fetchPageJpql(int start, int end);

    @NativeQuery(value = """
                         SELECT b.name AS name, b.age AS age, b.total AS total 
                             FROM (
                                 SELECT a.name AS name, 
                                        a.age AS age, 
                                        COUNT(*) OVER() AS total,
                                        ROW_NUMBER() OVER (ORDER BY a.age ASC, a.id ASC) AS row_num 
                                 FROM author a
                             ) b 
                             WHERE b.row_num BETWEEN ?1 AND ?2
                             ORDER BY b.row_num ASC
                         """)
    List<AuthorDto> fetchPageNative(int start, int end);
}
