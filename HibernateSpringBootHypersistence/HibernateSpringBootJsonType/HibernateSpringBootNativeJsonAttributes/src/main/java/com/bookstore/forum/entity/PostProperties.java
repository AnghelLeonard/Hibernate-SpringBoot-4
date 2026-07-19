package com.bookstore.forum.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO JSON payload for a forum thread. Unlike the JsonType module, this
 * plain POJO needs no {@code Serializable} marker, since the Hibernate-native
 * JSON mapping serializes it straight through the Jackson {@code FormatMapper}.
 */
public class PostProperties {

    private boolean pinned;

    private String flair;

    private List<String> attachments = new ArrayList<>();

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

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }
}
