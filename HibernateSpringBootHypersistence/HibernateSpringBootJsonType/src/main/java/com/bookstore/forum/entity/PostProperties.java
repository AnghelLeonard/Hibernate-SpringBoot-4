package com.bookstore.forum.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON payload for a forum thread. Serializable because the default
 * JsonType serializer clones mutable attribute values through Java
 * serialization. Deliberately without equals/hashCode, so the dirty-checking
 * tests reveal how each JSON mapping decides whether the attribute has
 * really changed.
 */
public class PostProperties implements Serializable {

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

    public PostProperties copy() {
        PostProperties properties = new PostProperties(flair);
        properties.setPinned(pinned);
        properties.setAttachments(new ArrayList<>(attachments));
        return properties;
    }
}
