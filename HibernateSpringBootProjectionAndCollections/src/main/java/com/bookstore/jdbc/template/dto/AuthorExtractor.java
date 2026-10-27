package com.bookstore.jdbc.template.dto;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class AuthorExtractor {

    private final JdbcTemplate jdbcTemplate;

    public AuthorExtractor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AuthorSummary> extract() {

        String sql = """
                     SELECT a.id, a.name, a.genre, b.id, b.title 
                     FROM author a INNER JOIN book b ON a.id = b.author_id
                     """;

        List<AuthorSummary> result = jdbcTemplate.query(sql, (ResultSet rs) -> {

            final Map<Long, AuthorSummary> authorsMap = new HashMap<>();
            while (rs.next()) {
                Long authorId = (rs.getLong("id"));
                AuthorSummary author = authorsMap.get(authorId);
                if (author == null) {
                    author = new AuthorSummary();
                    author.setId(rs.getLong("id"));
                    author.setName(rs.getString("name"));
                    author.setGenre(rs.getString("genre"));
                }

                BookSummary book = new BookSummary();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));

                author.addBook(book);
                authorsMap.putIfAbsent(author.getId(), author);
            }

            return new ArrayList<>(authorsMap.values());
        });

        return result;
    }
}
