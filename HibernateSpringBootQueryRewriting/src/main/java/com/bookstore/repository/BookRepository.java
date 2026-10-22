package com.bookstore.repository;

import com.bookstore.entity.Book;
import com.bookstore.rewriter.FullTextQueryRewriter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {

    @NativeQuery(value = """
                         SELECT * FROM book WHERE MATCH(title, content) AGAINST(:keyword _SEARCH_MODE_) 
                         ORDER BY MATCH(title, content) AGAINST(:keyword _SEARCH_MODE_) ASC
                         """,
            queryRewriter = FullTextQueryRewriter.class)
    List<Book> fetchViaFTS(@Param("keyword") String keyword);
}
