package com.bookstore.dto;

import java.util.List;

public class WindowDto<T> {

    private final List<T> content;
    private final String nextOffset;
    private final boolean hasNext;

    public WindowDto(List<T> content, String nextOffset, boolean hasNext) {
        this.content = content;
        this.nextOffset = nextOffset;
        this.hasNext = hasNext;
    }

    public List<T> getContent() {
        return content;
    }

    public String getNextOffset() {
        return nextOffset;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
