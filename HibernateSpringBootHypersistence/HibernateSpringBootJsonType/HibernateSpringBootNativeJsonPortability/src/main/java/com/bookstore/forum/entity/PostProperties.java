package com.bookstore.forum.entity;

/**
 * A POJO JSON payload for a forum thread, serialized by the Hibernate-native
 * JSON mapping through the Jackson {@code FormatMapper}.
 */
public class PostProperties {

    private boolean pinned;

    private String flair;

    public PostProperties() {
    }

    public PostProperties(String flair) {
        this.flair = flair;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public String getFlair() {
        return flair;
    }

    public void setFlair(String flair) {
        this.flair = flair;
    }
}
