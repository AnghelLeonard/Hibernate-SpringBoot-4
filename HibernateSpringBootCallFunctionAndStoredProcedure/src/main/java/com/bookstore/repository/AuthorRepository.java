package com.bookstore.repository;

import com.bookstore.dto.GenreAndAgeDto;
import com.bookstore.dto.GenreComparisonDto;
import com.bookstore.dto.NicknameAndAgeDto;
import com.bookstore.dto.NicknameAndAgeDtoI;
import com.bookstore.dto.NicknameGenreAndAgeDtoI;
import com.bookstore.entity.Author;
import com.bookstore.two.refcursor.AuthorRepositoryCustom;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorRepositoryCustom {

    @NativeQuery(value = "SELECT o_nickname AS nickname, o_age AS age FROM fetch_nickname_and_age_by_id_func(:p_id)")
    NicknameAndAgeDtoI fetchNicknameAndAgeByIdFunc(@Param("p_id") Integer id);
    
    /* this is needed if you map the result set to a Java record
    Optional<NicknameAndAgeDto> fetchRawNicknameAndAgeByIdFunc(@Param("p_id") Integer id);

    default Optional<NicknameAndAgeDto> fetchNicknameAndAgeByIdFunc(Integer id) {
        return fetchRawNicknameAndAgeByIdFunc(id)
                .filter(p -> p.nickname() != null || p.age() != null) // Handles empty row when ID is not found
                .map(p -> new NicknameAndAgeDto(p.nickname(), p.age()));
    }*/
    
    @Procedure(name = "Author.fetchNicknameAndAgeById")
    NicknameAndAgeDtoI fetchNicknameAndAgeById(@Param("p_id") Integer id);
    
    /* this is needed if you map the result set to a Java record        
    Map<String, Object> fetchNicknameAndAgeByIdRaw(@Param("p_id") Integer id);

    default Optional<NicknameAndAgeDto> fetchNicknameAndAgeById(Integer id) {
        Map<String, Object> out = fetchNicknameAndAgeByIdRaw(id);
        
        if (out == null || (out.get("o_nickname") == null && out.get("o_age") == null)) {
            return Optional.empty();
        }
        
        return Optional.of(new NicknameAndAgeDto(
                (String) out.get("o_nickname"),
                out.get("o_age") != null ? ((Number) out.get("o_age")).intValue() : null
        ));
    }*/

    @Procedure(name = "Author.fetchNicknameAndAgeByGenre")
    List<NicknameAndAgeDtoI> fetchNicknameAndAgeByGenre(@Param("p_genre") String genre);   
    
    /* this is needed if you map the result set to a Java record        
    List<Object[]> fetchRawNicknameAndAgeByGenre(@Param("p_genre") String genre);
    
    default List<NicknameAndAgeDto> fetchNicknameAndAgeByGenre(String genre) {
        return fetchRawNicknameAndAgeByGenre(genre).stream()
                .map(row -> new NicknameAndAgeDto(
                (String) row[0],
                row[1] != null ? ((Number) row[1]).intValue() : null
        )).toList();     
    }*/
    
    @NativeQuery(value = "SELECT nickname, age FROM fetch_nickname_and_age_by_genre_func(:p_genre)")
    List<NicknameAndAgeDtoI> fetchNicknameAndAgeByGenreFunc(@Param("p_genre") String genre);
            
    /* this is needed if you map the result set to a Java record   
    List<Object[]> fetchRawNicknameAndAgeByGenreFunc(@Param("p_genre") String genre);

    default List<NicknameAndAgeDto> fetchNicknameAndAgeByGenreFunc(String genre) {
        return fetchRawNicknameAndAgeByGenreFunc(genre).stream()
                .map(row -> new NicknameAndAgeDto(
                        (String) row[0],
                        row[1] != null ? ((Number) row[1]).intValue() : null
                ))
                .toList();
    }*/
    
    @Procedure(name = "Author.fetchAuthorByGenre")
    List<Author> fetchAuthorByGenre(@Param("p_genre") String genre);
    
    @NativeQuery(value = "SELECT * FROM fetch_author_by_genre_func(:p_genre)")
    List<Author> fetchAuthorByGenreFunc(@Param("p_genre") String genre);
    
    @NativeQuery(value = """
            SELECT is_matching AS isMatching,
                   nickname    AS nickname,
                   genre       AS genre,
                   age         AS age
            FROM fetch_nickname_genre_and_age_by_genre_table_func(:p_genre)
            """
    )
    List<NicknameGenreAndAgeDtoI> fetchRawGenreComparison(@Param("p_genre") String genre);

    default GenreComparisonDto fetchNicknameGenreAndAgeByGenreTableFunc(String genre) {
        Map<Boolean, List<NicknameGenreAndAgeDtoI>> partitioned = fetchRawGenreComparison(genre)
                .stream()
                .collect(Collectors.partitioningBy(row -> Boolean.TRUE.equals(row.getIsMatching())));

        List<NicknameAndAgeDto> matching = partitioned.get(true).stream()
                .map(row -> new NicknameAndAgeDto(row.getNickname(), row.getAge()))
                .toList();

        List<GenreAndAgeDto> others = partitioned.get(false).stream()
                .map(row -> new GenreAndAgeDto(row.getGenre(), row.getAge()))
                .toList();

        return new GenreComparisonDto(matching, others);
    }
}
