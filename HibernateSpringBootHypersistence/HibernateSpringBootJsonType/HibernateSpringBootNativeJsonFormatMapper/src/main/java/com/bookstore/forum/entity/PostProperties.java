package com.bookstore.forum.entity;

/**
 * A POJO JSON payload with deliberately camelCase field names, so the effect of
 * the custom {@code snake_case} native format mapper is visible in the stored
 * JSON ({@code flairLabel} &rarr; {@code flair_label},
 * {@code pinnedByModerator} &rarr; {@code pinned_by_moderator}). Unlike the
 * {@code JsonType} variant, the native mapping does not require the payload to be
 * {@code Serializable}.
 */
public class PostProperties {

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
