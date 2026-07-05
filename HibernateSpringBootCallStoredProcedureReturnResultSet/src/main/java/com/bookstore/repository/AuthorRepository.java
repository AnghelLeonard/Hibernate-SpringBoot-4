package com.bookstore.repository;

import com.bookstore.dto.AuthorDtoC;
import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.dto.AuthorDtoI;

@Repository
@Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {
        
    @Procedure(procedureName = "FETCH_AUTHOR_BY_GENRE")
    List<Author> fetchAuthorByGenre(@Param("p_genre") String genre);          
    
    @Procedure(procedureName = "FETCH_AUTHOR_ID_AND_NAME_BY_GENRE")
    List<AuthorDtoI> fetchAuthorIdNameByGenre(@Param("p_genre") String genre);   

    @Procedure(name = "FetchAuthorByGenre")
    List<Author> fetchAuthorByGenreNSPQ(@Param("p_genre") String genre);    
    
    @Procedure(name = "FetchAuthorIdNameByGenre")
    List<AuthorDtoC> fetchAuthorIdNameByGenreNSPQ(@Param("p_genre") String genre);
}
