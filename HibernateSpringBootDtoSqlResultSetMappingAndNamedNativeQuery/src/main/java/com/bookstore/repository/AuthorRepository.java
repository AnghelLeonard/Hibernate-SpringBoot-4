package com.bookstore.repository;

import com.bookstore.dto.AuthorDto;
import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @NativeQuery(name = "AuthorsNameQuery")
    List<String> fetchName();

    @NativeQuery(name = "AuthorDtoQuery")
    List<AuthorDto> fetchNameAndAge(); // or, AuthorRecord
}
