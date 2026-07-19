package com.bookstore.forum.entity;

import java.io.Serializable;

/**
 * A POJO JSON payload for a forum thread. Serializable because the default
 * JsonType serializer clones mutable attribute values through Java
 * serialization.
 */
public class PostProperties implements Serializable {

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
