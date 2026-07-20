package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.type.basic.YearMonthDateType;
import io.hypersistence.utils.hibernate.type.basic.YearMonthIntegerType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.time.YearMonth;

/**
 * Details of a forum thread that carry {@link YearMonth} attributes. Hibernate
 * 7.3 has no built-in mapping for {@code java.time.YearMonth}, so Hypersistence
 * Utils supplies two vendor-agnostic options that work on MySQL (and elsewhere):
 *
 * <ul>
 *   <li>{@link YearMonthIntegerType} stores the year-month as a compact integer
 *       ({@code 202607} for July 2026) &mdash; the {@code publishedOn} field maps
 *       to a {@code mediumint} column. Cheap to store and to compare/order.</li>
 *   <li>{@link YearMonthDateType} stores it as a {@code date} anchored on the
 *       first day of the month &mdash; the {@code archivedOn} field. Handy when
 *       the column must be a real date for reporting or joins.</li>
 * </ul>
 *
 * <p>Both round-trip to the exact same {@link YearMonth} in Java; the difference
 * is purely the physical column type.</p>
 */
@Entity
@Table(name = "year_month_post_details")
public class PostDetails {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Type(YearMonthIntegerType.class)
    @Column(name = "published_on", columnDefinition = "mediumint")
    private YearMonth publishedOn;

    @Type(YearMonthDateType.class)
    @Column(name = "archived_on", columnDefinition = "date")
    private YearMonth archivedOn;

    public PostDetails() {
    }

    public PostDetails(String title) {
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

    public YearMonth getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(YearMonth publishedOn) {
        this.publishedOn = publishedOn;
    }

    public YearMonth getArchivedOn() {
        return archivedOn;
    }

    public void setArchivedOn(YearMonth archivedOn) {
        this.archivedOn = archivedOn;
    }
}
