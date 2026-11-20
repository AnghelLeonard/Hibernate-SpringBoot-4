package com.bookstore.service;

import com.bookstore.dto.PurgeDto;
import com.bookstore.repository.AuthorRepository;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final JdbcTemplate jdbcTemplate;

    public BookstoreService(AuthorRepository authorRepository, JdbcTemplate jdbcTemplate) {

        this.authorRepository = authorRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = Propagation.NEVER)
    public Integer migrateGenreInBatchesJt(String oldGenre, String newGenre, int minAge, int batchSize) {
        List<SqlParameter> declaredParameters = Arrays.asList(
                new SqlParameter("old_genre", Types.VARCHAR),
                new SqlParameter("new_genre", Types.VARCHAR),
                new SqlParameter("min_age", Types.INTEGER),
                new SqlParameter("batch_size", Types.INTEGER),
                new SqlOutParameter("total_updated", Types.INTEGER)
        );

        CallableStatementCreator csc = con -> {
            CallableStatement cs = con.prepareCall("CALL migrate_author_genre_in_batches(?, ?, ?, ?, ?)");
            cs.setString(1, oldGenre);
            cs.setString(2, newGenre);
            cs.setInt(3, minAge);
            cs.setInt(4, batchSize);
            cs.registerOutParameter(5, Types.INTEGER);
            return cs;
        };

        Map<String, Object> result = jdbcTemplate.call(connection -> {
            // ensure autoCommit is true (default for non-transactional connections)
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
            
            return csc.createCallableStatement(connection);
        }, declaredParameters);
        return (Integer) result.get("total_updated");
    }

    @Transactional(propagation = Propagation.NEVER)
    public PurgeResult purgeAuthors(int minAge, int batchSize) {
        List<SqlParameter> parameters = List.of(
                new SqlParameter("min_age", Types.INTEGER),
                new SqlParameter("batch_size", Types.INTEGER),
                new SqlOutParameter("deleted_authors", Types.INTEGER),
                new SqlOutParameter("deleted_books", Types.INTEGER)
        );

        Map<String, Object> result = jdbcTemplate.call(connection -> {
            // ensure autoCommit is true (default for non-transactional connections)
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }

            CallableStatement cs = connection.prepareCall(
                    "CALL purge_authors_by_age_in_batches(?, ?, ?, ?)"
            );
            cs.setInt(1, minAge);
            cs.setInt(2, batchSize);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.INTEGER);
            return cs;
        }, parameters);

        int deletedAuthors = (Integer) result.get("deleted_authors");
        int deletedBooks = (Integer) result.get("deleted_books");

        return new PurgeResult(deletedAuthors, deletedBooks);
    }

    public record PurgeResult(int deletedAuthors, int deletedBooks) {}

    public int callMigrateGenreInBatchesJt() {
        return migrateGenreInBatchesJt("Horror", "Thriller", 25, 10);
    }
    
    public PurgeResult callPurgeAuthorsJt() {
        return purgeAuthors(32, 10);
    }

    public int runGenreMigration() {
        return authorRepository.migrateAuthorGenreInBatches("Horror", "Thriller", 25, 10);
    }

    public int runGenreMigrationProcedure() {
        return authorRepository.migrateAuthorGenreInBatchesProcedure("Thriller", "Horror", 25, 10);
    }

    public PurgeDto runPurgeAuthorsByAgeInBatches() {
        return authorRepository.purgeAuthorsByAgeInBatches(25, 10);
    }

    public Map<String, Object> runPurgeAuthorsByAgeInBatchesProcedure() {
        return authorRepository.purgeAuthorsByAgeInBatchesProcedure(20, 10);
    }
}
