package com.bookstore.forum.entity;

/**
 * A Markdown paragraph in a forum post body.
 */
public final class TextBlock extends PostContentBlock {

    public static final String TYPE = "text";

    private String markdown;

    public TextBlock() {
    }

    public TextBlock(String markdown) {
        this.markdown = markdown;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }
}
