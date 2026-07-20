package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.array.LocalDateArrayType;
import io.hypersistence.utils.hibernate.type.array.MultiDimensionalArrayType;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import io.hypersistence.utils.hibernate.type.array.UUIDArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A forum thread whose array attributes are mapped with Hypersistence Utils'
 * array types. These give explicit control over the SQL array element type and,
 * critically, cover cases the Hibernate-native array mapping does not: native
 * PostgreSQL enum arrays ({@code post_status[]}) and multidimensional arrays
 * ({@code integer[][]}).
 */
@Entity
@Table(name = "hypersistence_array_post")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Type(StringArrayType.class)
    @Column(name = "keywords", columnDefinition = "text[]")
    private String[] keywords;

    @Type(ListArrayType.class)
    @Column(name = "topics", columnDefinition = "text[]")
    private List<String> topics = new ArrayList<>();

    @Type(IntArrayType.class)
    @Column(name = "scores", columnDefinition = "integer[]")
    private int[] scores;

    @Type(LocalDateArrayType.class)
    @Column(name = "publication_dates", columnDefinition = "date[]")
    private LocalDate[] publicationDates;

    @Type(UUIDArrayType.class)
    @Column(name = "editor_ids", columnDefinition = "uuid[]")
    private UUID[] editorIds;

    // tag::enum-array[]
    @Type(
        value = EnumArrayType.class,
        parameters = @Parameter(name = EnumArrayType.SQL_ARRAY_TYPE, value = "post_status")
    )
    @Column(name = "status_history", columnDefinition = "post_status[]")
    private PostStatus[] statusHistory;

    @Type(MultiDimensionalArrayType.class)
    @Column(name = "rating_matrix", columnDefinition = "integer[][]")
    private Integer[][] ratingMatrix;
    // end::enum-array[]

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

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
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

    public PostStatus[] getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(PostStatus[] statusHistory) {
        this.statusHistory = statusHistory;
    }

    public Integer[][] getRatingMatrix() {
        return ratingMatrix;
    }

    public void setRatingMatrix(Integer[][] ratingMatrix) {
        this.ratingMatrix = ratingMatrix;
    }
}
