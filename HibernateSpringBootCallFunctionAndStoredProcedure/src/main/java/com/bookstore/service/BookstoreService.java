package com.bookstore.service;

import com.bookstore.dto.AuthorGenreResponse;
import com.bookstore.dto.NicknameAndAgeDto;
import com.bookstore.dto.GenreAndAgeDto;
import com.bookstore.dto.GenreComparisonDto;
import com.bookstore.dto.NicknameAndAgeDtoI;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import jakarta.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final JdbcTemplate jdbcTemplate;
    private final AuthorRepository authorRepository;

    public BookstoreService(JdbcTemplate jdbcTemplate, AuthorRepository authorRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorRepository = authorRepository;
    }

    @PostConstruct
    void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }

    // call fetch_author_by_genre()
    @Transactional(readOnly = true)
    public List<Author> fetchAnthologyAuthors() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("fetch_author_by_genre")
                .returningResultSet("p_cursor", new BeanPropertyRowMapper<>(Author.class));

        Map<String, Object> out = jdbcCall.execute(Map.of("p_genre", "Anthology"));

        return (List<Author>) out.get("p_cursor");
    }

    // call fetch_author_by_genre_func()    
    public List<Author> fetchFuncAnthologyAuthors() {
        String sql = "SELECT * FROM fetch_author_by_genre_func(?)";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Author.class),
                "Anthology"
        );
    }

    // call fetch_nickname_and_age_by_genre()
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<NicknameAndAgeDto> fetchNicknameAndAgeByGenre() {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("fetch_nickname_and_age_by_genre")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_genre", Types.VARCHAR),
                        new SqlInOutParameter("p_cursor", Types.REF_CURSOR, (rs, rowNum)
                                -> new NicknameAndAgeDto(rs.getString("nickname"), rs.getInt("age")
                        )));

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("p_genre", "Anthology");
        inParams.put("p_cursor", null);

        Map<String, Object> out = simpleJdbcCall.execute(inParams);
        return (List<NicknameAndAgeDto>) out.get("p_cursor");
    }

    // call fetch_nickname_and_age_by_genre_func()
    public List<NicknameAndAgeDto> fetchNicknameAndAgeByGenreFunc() {
        String sql = "SELECT nickname, age FROM fetch_nickname_and_age_by_genre_func(?)";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new NicknameAndAgeDto(
                        rs.getString("nickname"),
                        rs.getInt("age")
                ),
                "Anthology"
        );
    }

    // call fetch_nickname_genre_and_age_by_genre()
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public AuthorGenreResponse fetchNicknameGenreAndAgeByGenre() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("fetch_nickname_genre_and_age_by_genre")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_genre", Types.VARCHAR),
                        new SqlInOutParameter("p_cursor_1", Types.REF_CURSOR, (rs, rowNum)
                                -> new NicknameAndAgeDto(rs.getString("nickname"), rs.getInt("age"))),
                        new SqlInOutParameter("p_cursor_2", Types.REF_CURSOR, (rs, rowNum)
                                -> new GenreAndAgeDto(rs.getString("genre"), rs.getInt("age")))
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("p_genre", "Anthology");
        inParams.put("p_cursor_1", null);
        inParams.put("p_cursor_2", null);

        Map<String, Object> result = jdbcCall.execute(inParams);

        return new AuthorGenreResponse(
                (List<NicknameAndAgeDto>) result.get("p_cursor_1"),
                (List<GenreAndAgeDto>) result.get("p_cursor_2")
        );
    }

    // call fetch_nickname_and_age_by_id()
    public NicknameAndAgeDto fetchNicknameAndAgeById() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("fetch_nickname_and_age_by_id")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("o_nickname", Types.VARCHAR),
                        new SqlOutParameter("o_age", Types.INTEGER)
                );

        Map<String, Object> out = jdbcCall.execute(Map.of("p_id", 1L));

        return new NicknameAndAgeDto(
                (String) out.get("o_nickname"),
                (Integer) out.get("o_age")
        );
    }

    // call fetch_nickname_and_age_by_id_func()
    public Optional<NicknameAndAgeDto> fetchNicknameAndAgeByIdFunc() {
        String sql = "SELECT o_nickname, o_age FROM fetch_nickname_and_age_by_id_func(?)";

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                String nickname = rs.getString("o_nickname");
                Integer age = rs.getObject("o_age", Integer.class);

                // PostgreSQL returns a row of (null, null) when SELECT INTO finds no match
                if (nickname == null && age == null) {
                    return Optional.empty();
                }
                return Optional.of(new NicknameAndAgeDto(nickname, age));
            }
            return Optional.empty();
        }, 1);
    }

    // call fetch_nickname_genre_and_age_by_genre_two_cursors_func()
    @Transactional(readOnly = true) // Mandatory to keep PostgreSQL cursors open while reading
    public GenreComparisonDto fetchNicknameGenreAndAgeByGenreTwoCursors() {
        String sql = "SELECT * FROM fetch_nickname_genre_and_age_by_genre_two_cursors_func(?)";

        return jdbcTemplate.query(sql, outerRs -> {
            List<NicknameAndAgeDto> matching = new ArrayList<>();
            List<GenreAndAgeDto> others = new ArrayList<>();

            // (cursor_matching_genre)
            if (outerRs.next()) {
                try (ResultSet rs1 = (ResultSet) outerRs.getObject(1)) {
                    while (rs1.next()) {
                        matching.add(new NicknameAndAgeDto(
                                rs1.getString("nickname"),
                                rs1.getObject("age", Integer.class)
                        ));
                    }
                }
            }

            // (cursor_other_genres)
            if (outerRs.next()) {
                try (ResultSet rs2 = (ResultSet) outerRs.getObject(1)) {
                    while (rs2.next()) {
                        others.add(new GenreAndAgeDto(
                                rs2.getString("genre"),
                                rs2.getObject("age", Integer.class)
                        ));
                    }
                }
            }

            return new GenreComparisonDto(matching, others);
        }, "Anthology");
    }

    // call fetch_nickname_genre_and_age_by_genre_table_func()
    public GenreComparisonDto fetchNicknameGenreAndAgeByGenreTable() {
        String sql = "SELECT is_matching, nickname, genre, age FROM fetch_nickname_genre_and_age_by_genre_table_func(?)";

        return jdbcTemplate.query(sql, rs -> {
            List<NicknameAndAgeDto> matching = new ArrayList<>();
            List<GenreAndAgeDto> others = new ArrayList<>();

            while (rs.next()) {
                if (rs.getBoolean("is_matching")) {
                    matching.add(new NicknameAndAgeDto(
                            rs.getString("nickname"),
                            rs.getObject("age", Integer.class)
                    ));
                } else {
                    others.add(new GenreAndAgeDto(
                            rs.getString("genre"),
                            rs.getObject("age", Integer.class)
                    ));
                }
            }
            return new GenreComparisonDto(matching, others);
        }, "Anthology");
    }

    // call fetch_nickname_and_age_by_id_func() via JpaRepository
    public NicknameAndAgeDtoI fetchNicknameAndAgeByIdRepo() {
        return authorRepository.fetchNicknameAndAgeByIdFunc(1);
    }

    // call fetch_nickname_and_age_by_id() via @Procedure
    public NicknameAndAgeDtoI fetchNicknameAndAgeByIdProc() {
        return authorRepository.fetchNicknameAndAgeById(1);
    }

    // call fetch_nickname_and_age_by_genre() via @Procedure
    @Transactional(readOnly = true) // this is not needed if you map the result set to a Java record 
    public List<NicknameAndAgeDtoI> fetchNicknameAndAgeByGenreProc() {
        return authorRepository.fetchNicknameAndAgeByGenre("Anthology");
    }

    // call fetch_nickname_and_age_by_genre_func() via JpaRepository
    public List<NicknameAndAgeDtoI> fetchNicknameAndAgeByGenreFuncRepo() {
        return authorRepository.fetchNicknameAndAgeByGenreFunc("Anthology");
    }

    // call fetch_nickname_genre_and_age_by_genre() via AuthorRepositoryCustom
    public AuthorGenreResponse fetchNicknameGenreAndAgeByGenreRepo() {
        return authorRepository.fetchNicknameGenreAndAgeByGenre("Anthology");
    }

    // call fetch_nickname_genre_and_age_by_genre_two_cursors_func() via AuthorRepositoryCustom    
    public AuthorGenreResponse fetchNicknameGenreAndAgeByGenreFuncRepo() {
        return authorRepository.fetchNicknameGenreAndAgeByGenreFunc("Anthology");
    }

    // call fetch_nickname_genre_and_age_by_genre_table_func() via JpaRepository
    public GenreComparisonDto fetchNicknameGenreAndAgeByGenreTableFuncRepo() {
        return authorRepository.fetchNicknameGenreAndAgeByGenreTableFunc("Anthology");
    }

    // call fetch_author_by_genre()
    @Transactional(readOnly = true)
    public List<Author> fetchAnthologyAuthorsRepo() {
        return authorRepository.fetchAuthorByGenre("Anthology");
    }

    // call fetch_author_by_genre_func()
    public List<Author> fetchAnthologyAuthorsFuncRepo() {
        return authorRepository.fetchAuthorByGenreFunc("Anthology");
    }
}
