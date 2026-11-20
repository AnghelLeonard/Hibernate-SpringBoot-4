package com.bookstore.repository;

import com.bookstore.dto.PurgeDto;
import com.bookstore.entity.Author;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
// DON'T ADD HERE @Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {
        
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @NativeQuery(value = "CALL migrate_author_genre_in_batches(:oldGenre, :newGenre, :minAge, :batchSize, NULL)")
    Integer migrateAuthorGenreInBatches(
            @Param("oldGenre") String oldGenre,
            @Param("newGenre") String newGenre,
            @Param("minAge") int minAge,
            @Param("batchSize") int batchSize
    );
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Procedure(name = "Author.migrateGenreInBatches")
    Integer migrateAuthorGenreInBatchesProcedure(
            String oldGenre,
            String newGenre,
            int minAge,
            int batchSize
    );
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @NativeQuery(value = "CALL purge_authors_by_age_in_batches(:minAge, :batchSize, NULL, NULL)")
    PurgeDto purgeAuthorsByAgeInBatches(
            @Param("minAge") int minAge,
            @Param("batchSize") int batchSize
    );
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Procedure(name = "Author.purgeByAgeInBatches")
    Map<String, Object> purgeAuthorsByAgeInBatchesProcedure(int minAge, int batchSize);        
}
