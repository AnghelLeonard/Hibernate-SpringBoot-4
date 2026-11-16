package com.bookstore.repository;

import com.bookstore.dto.AuthorBookCountFlatDto;
import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.dto.AuthorBookFlatDtoKeyset;
import com.bookstore.entity.Author;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = """
                   SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
                   FROM Author a 
                   JOIN a.books b WHERE a.genre = ?1
                   """,
            countQuery = """
                   SELECT COUNT(b) 
                   FROM Book b 
                   WHERE b.author IN (SELECT a FROM Author a WHERE a.genre = ?1)                        
                   """)
    // the Spring Data COUNT query generated here contains JOINs and it represents a performance penalty
    // if we override countQuery to strip JOINs we risk totalElements inconsistencies across pages
    // so pay attention how you decide to override countQuery
    Page<AuthorBookFlatDto> fetchPageOfDto(String genre, Pageable pageable);

    @Query(value = """
                   SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
                   FROM Author a 
                   JOIN a.books b WHERE a.genre = ?1
                   """)
    Slice<AuthorBookFlatDto> fetchSliceOfDto(String genre, Pageable pageable);

    @Query(value = """
                   SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
                   FROM Author a 
                   JOIN a.books b WHERE a.genre = ?1
                   """)
    List<AuthorBookFlatDto> fetchListOfDto(String genre, Pageable pageable);

    @Query(value = """
                   SELECT a.name AS name,a.age AS age,b.title AS title,b.isbn AS isbn,
                          COUNT(*) OVER() AS total
                   FROM Author a
                   JOIN a.books b WHERE a.genre = ?1
                   """)
    List<AuthorBookCountFlatDto> fetchListWithCountOfDto(String genre, Pageable pageable);

    // DENSE_RANK() approach
    @Query("""
        SELECT sub.name, sub.age, sub.title, sub.isbn
            FROM (
                SELECT a.name AS name, 
                       a.age AS age, 
                       b.title AS title, 
                       b.isbn AS isbn,
                       a.id AS authorId,
                       DENSE_RANK() OVER (ORDER BY a.name ASC, a.id ASC) AS pos
                FROM Author a
                JOIN a.books b
                WHERE a.genre = :genre
            ) sub
            WHERE sub.pos > :offset AND sub.pos <= (:offset + :size)
            ORDER BY sub.name ASC, sub.authorId ASC, sub.title ASC
    """)
    List<AuthorBookFlatDto> fetchListOfDtoDenseRank(
            @Param("genre") String genre, @Param("offset") int offset, @Param("size") int size);

    @NativeQuery(value = """
        SELECT sub.name AS name, sub.age AS age, sub.title AS title, sub.isbn AS isbn
            FROM (
                SELECT a.name AS name, 
                       a.age AS age, 
                       b.title AS title, 
                       b.isbn AS isbn,
                       a.id AS author_id,
                       DENSE_RANK() OVER (ORDER BY a.name ASC, a.id ASC) AS pos
                FROM author a
                JOIN book b ON a.id = b.author_id
                WHERE a.genre = :genre
            ) sub
            WHERE sub.pos > :offset AND sub.pos <= (:offset + :size)
            ORDER BY sub.name ASC, sub.author_id ASC, sub.title ASC
    """)
    List<AuthorBookFlatDto> fetchListOfDtoDenseRankNative(
            @Param("genre") String genre, @Param("offset") long offset, @Param("size") int size);

    // EXISTS approach
    @Query("""
        SELECT a.name, a.age, b.title, b.isbn
        FROM Author a
        JOIN a.books b
        WHERE a.id IN (
            SELECT a2.id
            FROM Author a2
            WHERE a2.genre = :genre
               AND EXISTS (SELECT 1 FROM Book b2 WHERE b2.author = a2)
            ORDER BY a2.name ASC, a2.id ASC
                   LIMIT :size OFFSET :offset
        )
        ORDER BY a.name ASC, a.id ASC, b.title ASC
    """)
    List<AuthorBookFlatDto> fetchListOfDtoExists(
            @Param("genre") String genre, @Param("offset") long offset, @Param("size") int size);

    @NativeQuery(value = """
        SELECT a.name AS name, a.age AS age, b.title AS title, b.isbn AS isbn
        FROM author a
        JOIN book b ON a.id = b.author_id
        WHERE a.id IN (
            SELECT a2.id
            FROM author a2
            WHERE a2.genre = :genre
              AND EXISTS (SELECT 1 FROM book b2 WHERE b2.author_id = a2.id)
            ORDER BY a2.name ASC, a2.id ASC
            LIMIT :size OFFSET :offset
        )
        ORDER BY a.name ASC, a.id ASC, b.title ASC
    """)
    List<AuthorBookFlatDto> fetchListOfDtoExistsNative(
            @Param("genre") String genre, @Param("offset") long offset, @Param("size") int size);

    // EXISTS with 2 queries - first query fetches authors IDs
    @Query("""
        SELECT a.id
        FROM Author a
        WHERE a.genre = :genre
          AND EXISTS (
              SELECT 1
              FROM Book b
              WHERE b.author = a
          )        
        ORDER BY a.name ASC, a.id ASC
        LIMIT :size OFFSET :offset
    """)
    List<Long> fetchListOfDtoExistsIds(
            @Param("genre") String genre, @Param("offset") long offset, @Param("size") int size);

    // second query fetches the authors and books
    @Query("""
        SELECT a.name AS name,
               a.age AS age,
               b.title AS title,
               b.isbn AS isbn
        FROM Author a
        JOIN a.books b
        WHERE a.id IN :ids
        ORDER BY a.name ASC,
                 a.id ASC,
                 b.title ASC
    """)
    List<AuthorBookFlatDto> fetchListOfDtoExistsBooks(@Param("ids") List<Long> ids);

    // keyset pagination approach
    @Query("""
        SELECT a.id, a.name, a.age, a.createdAt, b.title, b.isbn
                FROM Author a
                JOIN a.books b
                WHERE a.id IN (
                    SELECT a2.id
                    FROM Author a2
                    WHERE a2.genre = :genre
                      AND EXISTS (SELECT 1 FROM Book b2 WHERE b2.author = a2)
                    ORDER BY a2.createdAt ASC, a2.id ASC
                    LIMIT :size
                )
                ORDER BY a.createdAt ASC, a.id ASC, b.title ASC
        """)
    List<AuthorBookFlatDtoKeyset> fetchFirstListOfDtoKeyset(
            @Param("genre") String genre, @Param("size") int size);

    @Query("""
        SELECT a.id, a.name, a.age, a.createdAt, b.title, b.isbn
        FROM Author a
        JOIN a.books b
        WHERE a.id IN (
            SELECT a2.id
            FROM Author a2
            WHERE a2.genre = :genre
              AND (a2.createdAt, a2.id) > (:lastCreatedAt, :lastId)
              AND EXISTS (SELECT 1 FROM Book b2 WHERE b2.author = a2)
            ORDER BY a2.createdAt ASC, a2.id ASC
            LIMIT :size
        )
        ORDER BY a.createdAt ASC, a.id ASC, b.title ASC
        """)
    List<AuthorBookFlatDtoKeyset> fetchNextListOfDtoKeyset(
            @Param("genre") String genre, @Param("lastCreatedAt") Instant lastCreatedAt,
            @Param("lastId") Long lastId, @Param("size") int size);

    // return total number of authors in a single round-trip
    // you can also go for a separate SELECT
    //@Query("SELECT COUNT(a) FROM Author a WHERE a.genre = ?1")
    // long countByGenre(String genre);
    @Query("""
        WITH RankedAuthorBooks AS (
                    SELECT
                        a.id AS authorId,
                        a.name AS authorName,
                        a.age AS authorAge,
                        b.id AS bookId,
                        b.title AS bookTitle,
                        b.isbn AS bookIsbn,
                        DENSE_RANK() OVER (ORDER BY a.name ASC, a.id ASC) AS pos
                    FROM Author a
                    JOIN a.books b
                    WHERE a.genre = :genre
                ),
                WithTotalRank AS (
                    SELECT
                        rab.authorId AS authorId,
                        rab.authorName AS authorName,
                        rab.authorAge AS authorAge,
                        rab.bookId AS bookId,
                        rab.bookTitle AS bookTitle,
                        rab.bookIsbn AS bookIsbn,
                        rab.pos AS pos,
                        MAX(rab.pos) OVER () AS maxRank
                    FROM RankedAuthorBooks rab
                )
                SELECT
                    wtr.authorId,
                    wtr.authorName,
                    wtr.authorAge,
                    wtr.bookId,
                    wtr.bookTitle,
                    wtr.bookIsbn,
                    wtr.maxRank
                FROM WithTotalRank wtr
                WHERE wtr.pos > :offset AND wtr.pos <= (:offset + :size)
                ORDER BY wtr.authorName ASC, wtr.authorId ASC, wtr.bookTitle ASC
    """)
    List<Object[]> fetchDenseRankedAndTotal(
            @Param("genre") String genre, @Param("offset") long offset, @Param("size") int size);

    @Query("""
    WITH RankedAuthors AS (
        SELECT 
            a2.id AS authorId,
            ROW_NUMBER() OVER (ORDER BY a2.name ASC, a2.id ASC) AS pos,
            COUNT(*) OVER () AS totalAuthors
        FROM Author a2
        WHERE a2.genre = :genre
          AND EXISTS (SELECT 1 FROM Book b2 WHERE b2.author = a2)
    )
    SELECT 
        a.id, 
        a.name, 
        a.age, 
        b.id, 
        b.title, 
        b.isbn,
        ra.totalAuthors AS maxRank
    FROM RankedAuthors ra
    JOIN Author a ON a.id = ra.authorId
    JOIN a.books b
    WHERE ra.pos > :offset AND ra.pos <= (:offset + :size)
    ORDER BY a.name ASC, a.id ASC, b.title ASC
    """)
    List<Object[]> fetchRowNumberAndTotal(
            @Param("genre") String genre, @Param("offset") long offset, @Param("size") int size);
}
