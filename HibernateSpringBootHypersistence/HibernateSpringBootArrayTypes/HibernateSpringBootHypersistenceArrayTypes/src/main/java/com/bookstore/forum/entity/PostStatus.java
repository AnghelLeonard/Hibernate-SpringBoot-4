package com.bookstore.forum.entity;

/**
 * The moderation lifecycle of a forum post. Stored inside a PostgreSQL native
 * enum-array column ({@code post_status[]}) via Hypersistence Utils'
 * {@code EnumArrayType}.
 */
public enum PostStatus {
    DRAFT, PUBLISHED, ARCHIVED
}
