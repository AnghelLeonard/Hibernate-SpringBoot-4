package com.bookstore.forum.entity;

import java.time.OffsetDateTime;

/**
 * A POJO JSON payload with deliberately camelCase field names and a couple of
 * {@code java.time} timestamps, so the effect of the custom native format mapper
 * is visible in the stored JSON: {@code flairLabel} &rarr; {@code flair_label}
 * and {@code createdOn} &rarr; {@code created_on} (snake_case), the ISO-8601
 * rendering of {@code createdOn} (Jackson 3 default), and the omission of the
 * {@code null} {@code editedOn} field ({@code NON_NULL} inclusion). Unlike the
 * {@code JsonType} variant, the native mapping does not require the payload to be
 * {@code Serializable}.
 */
public class PostProperties {

    private String flairLabel;

    private boolean pinnedByModerator;

    private OffsetDateTime createdOn;

    private OffsetDateTime editedOn;

    public PostProperties() {
    }

    public PostProperties(String flairLabel, boolean pinnedByModerator, OffsetDateTime createdOn) {
        this.flairLabel = flairLabel;
        this.pinnedByModerator = pinnedByModerator;
        this.createdOn = createdOn;
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

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public OffsetDateTime getEditedOn() {
        return editedOn;
    }

    public void setEditedOn(OffsetDateTime editedOn) {
        this.editedOn = editedOn;
    }
}
