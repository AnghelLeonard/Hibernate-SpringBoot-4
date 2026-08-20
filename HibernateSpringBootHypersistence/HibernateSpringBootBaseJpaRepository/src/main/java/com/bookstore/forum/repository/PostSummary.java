package com.bookstore.forum.repository;

/**
 * DTO projection carrying only the columns the "most-viewed approved posts"
 * query actually needs (title and view count). A Spring Data query that returns
 * this record is translated into a {@code select p.title, p.views} &mdash; the
 * projection is performed by the database, not by loading whole {@code Post}
 * entities and discarding their other columns in Java.
 *
 * <p>The same record is reused by the in-memory anti-pattern, so the two results
 * compare as equal even though one was computed in SQL and the other in a
 * {@code Stream}.</p>
 */
public record PostSummary(String title, long views) {
}
