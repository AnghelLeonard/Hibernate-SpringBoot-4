package com.bookstore.repository;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
        
    @Procedure(value = "FETCH_AUTHOR_ID_AND_NAME_BY_GENRE")
    List<AuthorDto> fetchAuthorByGenre(@Param("p_genre") String genre);          
    
    @Procedure(name = "FetchAuthorIdNameByGenre")
    Object[] fetchAuthorIdNameByGenre(@Param("p_genre") String genre);          
}
