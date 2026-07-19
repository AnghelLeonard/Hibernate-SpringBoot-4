package com.bookstore.forum.entity;

/**
 * An embedded image in a forum post body.
 */
public final class ImageBlock extends PostContentBlock {

    public static final String TYPE = "image";

    private String url;

    private String alt;

    public ImageBlock() {
    }

    public ImageBlock(String url, String alt) {
        this.url = url;
        this.alt = alt;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
