package com.bookstore.repository;

import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
           
   // @Procedure(name = "CountByGenreProcedure")
   // Integer countByGenre(@Param("p_genre") String genre); 
        
    @Procedure(procedureName = "count_author_by_genre", outputParameterName = "p_count")
    Integer countByGenre(@Param("p_genre") String genre);
    
    @NativeQuery(value = "SELECT count_author_by_genre_func(:p_genre)")
    Integer countByGenreFunc(@Param("p_genre") String genre);
}
