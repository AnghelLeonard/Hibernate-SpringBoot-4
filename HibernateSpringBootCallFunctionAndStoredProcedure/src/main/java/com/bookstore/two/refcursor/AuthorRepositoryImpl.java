package com.bookstore.two.refcursor;

import com.bookstore.dto.AuthorGenreResponse;
import com.bookstore.dto.GenreAndAgeDto;
import com.bookstore.dto.NicknameAndAgeDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class AuthorRepositoryImpl implements AuthorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public AuthorGenreResponse fetchNicknameGenreAndAgeByGenre(String genre) {
        StoredProcedureQuery query = entityManager
                .createNamedStoredProcedureQuery("Author.fetchNicknameGenreAndAgeByGenre");

        query.setParameter("p_genre", genre);
        query.execute();

        // fetch rows from the first cursor
        List<Object[]> cursor1Rows = query.getResultList();
        List<NicknameAndAgeDto> matching = cursor1Rows.stream()
                .map(row -> new NicknameAndAgeDto(
                (String) row[0],
                row[1] != null ? ((Number) row[1]).intValue() : null
        ))
                .toList();

        // fetch rows from the second cursor
        List<GenreAndAgeDto> others = List.of();
        if (query.hasMoreResults()) {
            List<Object[]> cursor2Rows = query.getResultList();
            others = cursor2Rows.stream()
                    .map(row -> new GenreAndAgeDto(
                    (String) row[0],
                    row[1] != null ? ((Number) row[1]).intValue() : null
            ))
                    .toList();
        }

        return new AuthorGenreResponse(matching, others);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public AuthorGenreResponse fetchNicknameGenreAndAgeByGenreFunc(String genre) {
        
        Session session = entityManager.unwrap(Session.class);

        return session.doReturningWork(connection -> {
            List<NicknameAndAgeDto> matching = new ArrayList<>();
            List<GenreAndAgeDto> others = new ArrayList<>();
          
            String sql = "SELECT * FROM fetch_nickname_genre_and_age_by_genre_two_cursors_func(?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, genre);

                try (ResultSet outerRs = ps.executeQuery()) {
                    // REFCURSOR (cursor_matching_genre)
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

                    // REFCURSOR (cursor_other_genres)
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
                }
            }

            return new AuthorGenreResponse(matching, others);
        });
    }
}
