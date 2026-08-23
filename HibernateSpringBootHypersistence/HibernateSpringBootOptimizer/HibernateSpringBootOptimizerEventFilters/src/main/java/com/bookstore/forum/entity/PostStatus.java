package com.bookstore.forum.entity;

/**
 * The post workflow status. It is stored in the legacy {@code filtered_post.status}
 * column by its name (a {@code VARCHAR}), not as an ordinal, so switching the
 * mapping to {@code EnumType.ORDINAL} would break every other application that
 * already reads and writes that column by name — see {@link Post#getStatus()}.
 */
public enum PostStatus {

    DRAFT,
    PUBLISHED,
    ARCHIVED
}
