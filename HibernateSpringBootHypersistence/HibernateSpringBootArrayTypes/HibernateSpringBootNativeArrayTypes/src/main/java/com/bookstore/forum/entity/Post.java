package com.bookstore.forum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A forum thread whose array attributes are mapped with Hibernate's built-in
 * (native) array support. On PostgreSQL, Hibernate maps a basic array or a
 * {@code Collection} of a basic type straight to a SQL array column, with no
 * extra annotation and no {@code columnDefinition} (the DDL is dialect-aware).
 *
 * <p>The companion test records what the native mapping covers: the simple,
 * single-dimension arrays here all round-trip, and Hibernate emits the
 * SQL-standard {@code <type> array} DDL for them ({@code uuid array},
 * {@code varchar(255) array}, {@code integer array}, {@code date array}). Two
 * cases are where the Hypersistence Utils module adds value (both verified while
 * building this example):
 * <ul>
 *   <li><strong>Multidimensional arrays are not supported at all</strong> &mdash;
 *       mapping an {@code Integer[][]} fails EntityManagerFactory startup with
 *       <em>"Nested arrays (with the exception of byte[][]) are not supported"</em>.</li>
 *   <li><strong>Enum arrays fall back to an ordinal array</strong> &mdash; a
 *       {@code PostStatus[]} maps to {@code smallint array}, not to a native
 *       PostgreSQL enum-typed array ({@code post_status[]}) the way
 *       Hypersistence Utils' {@code EnumArrayType} does.</li>
 * </ul>
 * The Hypersistence Utils array module also gives explicit control of the SQL
 * element type via {@code columnDefinition} (e.g. {@code text[]} rather than the
 * native {@code varchar(255) array}).</p>
 */
@Entity
@Table(name = "native_array_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String[] keywords;

    private List<String> topics = new ArrayList<>();

    private Integer[] scores;

    private LocalDate[] publicationDates;

    private UUID[] editorIds;

    public Post() {
    }

    public Post(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Integer[] getScores() {
        return scores;
    }

    public void setScores(Integer[] scores) {
        this.scores = scores;
    }

    public LocalDate[] getPublicationDates() {
        return publicationDates;
    }

    public void setPublicationDates(LocalDate[] publicationDates) {
        this.publicationDates = publicationDates;
    }

    public UUID[] getEditorIds() {
        return editorIds;
    }

    public void setEditorIds(UUID[] editorIds) {
        this.editorIds = editorIds;
    }
}
