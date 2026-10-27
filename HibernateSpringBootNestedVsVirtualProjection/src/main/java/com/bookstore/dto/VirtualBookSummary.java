package com.bookstore.dto;

import org.springframework.beans.factory.annotation.Value;

public interface VirtualBookSummary {
    
    public String getTitle(); // of book
    public Integer getRank(); // of rank
    
    @Value("#{@authorMapper.buildAuthorSummary(target.name, target.genre)}")
    AuthorClassSummary getAuthor();
}
