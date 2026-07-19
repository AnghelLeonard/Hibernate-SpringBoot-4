package com.bookstore.forum.repository;

/**
 * A read-only DTO projection whose {@code source} value is pulled straight out
 * of the {@code raw_payload} JSON column with {@code JSON_EXTRACT}, without
 * loading the whole entity or deserializing the JSON on the Java side.
 */
public interface PostSummary {

    String getTitle();

    String getSource();
}
