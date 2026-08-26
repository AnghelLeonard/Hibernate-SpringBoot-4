package com.bookstore.repository;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.AuthorRecord;
import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    @Query(value="SELECT new com.bookstore.dto.AuthorDto(a.name, a.age) FROM Author a")
    List<AuthorDto> fetchAuthorsDto();

    @Query(value="SELECT new com.bookstore.dto.AuthorRecord(a.name, a.age) FROM Author a")
    List<AuthorRecord> fetchAuthorsRecord();

}
