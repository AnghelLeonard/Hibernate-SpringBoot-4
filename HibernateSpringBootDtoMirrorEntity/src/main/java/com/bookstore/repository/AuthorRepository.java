package com.bookstore.repository;

import com.bookstore.dto.AuthorSummary;
import com.bookstore.dto.RecordAuthorSummary;
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

    List<AuthorSummary> findBy();

    @Query("SELECT a FROM Author a")
    List<AuthorSummary> fetchAsDto();
    
    @Query("SELECT a FROM Author a")
    List<RecordAuthorSummary> fetchAsRecordDto();

    @Query("""
           SELECT a.id AS id, a.age AS age, a.name AS name,
                  a.genre AS genre, a.address AS address, a.rank AS rank,
                  a.nickname AS nickname, a.language AS language
           FROM Author a
           """)
    List<AuthorSummary> fetchAsDtoColumns();

    @NativeQuery("""
           SELECT id, age, name, genre, address, rank, nickname, language
           FROM Author a
           """)
    List<AuthorSummary> fetchAsDtoNative();

}
