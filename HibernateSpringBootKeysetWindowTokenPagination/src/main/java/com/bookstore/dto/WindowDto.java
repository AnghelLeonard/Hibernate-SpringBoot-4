package com.bookstore.dto;

import java.util.List;

public class WindowDto<T> {

    private final List<T> content;
    private final String nextToken;
    private final boolean hasNext;

    public WindowDto(List<T> content, String nextToken, boolean hasNext) {
        this.content = content;
        this.nextToken = nextToken;
        this.hasNext = hasNext;
    }
  
    public List<T> getContent() {
        return content;
    }

    public String getNextToken() {
        return nextToken;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
