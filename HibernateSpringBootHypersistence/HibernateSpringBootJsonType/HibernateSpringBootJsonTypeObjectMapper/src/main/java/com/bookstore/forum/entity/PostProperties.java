package com.bookstore.forum.entity;

import java.io.Serializable;

/**
 * A POJO JSON payload with deliberately camelCase field names, so the effect of
 * the custom {@code snake_case} ObjectMapper is visible in the stored JSON
 * ({@code flairLabel} &rarr; {@code flair_label},
 * {@code pinnedByModerator} &rarr; {@code pinned_by_moderator}). Serializable
 * because the default JsonType serializer clones mutable values through Java
 * serialization.
 */
public class PostProperties implements Serializable {

    private String flairLabel;

    private boolean pinnedByModerator;

    public PostProperties() {
    }

    public PostProperties(String flairLabel, boolean pinnedByModerator) {
        this.flairLabel = flairLabel;
        this.pinnedByModerator = pinnedByModerator;
    }

    public String getFlairLabel() {
        return flairLabel;
    }

    public void setFlairLabel(String flairLabel) {
        this.flairLabel = flairLabel;
    }

    public boolean isPinnedByModerator() {
        return pinnedByModerator;
    }

    public void setPinnedByModerator(boolean pinnedByModerator) {
        this.pinnedByModerator = pinnedByModerator;
    }
}
