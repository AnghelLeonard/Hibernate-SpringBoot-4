package com.bookstore.forum.entity;

/**
 * A syntax-highlighted code snippet in a forum post body.
 */
public final class CodeBlock extends PostContentBlock {

    public static final String TYPE = "code";

    private String language;

    private String source;

    public CodeBlock() {
    }

    public CodeBlock(String language, String source) {
        this.language = language;
        this.source = source;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
