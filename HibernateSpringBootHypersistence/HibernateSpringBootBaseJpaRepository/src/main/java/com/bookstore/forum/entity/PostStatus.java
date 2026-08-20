package com.bookstore.forum.entity;

/**
 * Moderation state of a {@link Post}. Used by the {@code findAll()} anti-pattern
 * example to show a filter that belongs in the {@code WHERE} clause, not in a
 * Java {@code Stream}.
 */
public enum PostStatus {

    PENDING,
    APPROVED,
    SPAM
}
