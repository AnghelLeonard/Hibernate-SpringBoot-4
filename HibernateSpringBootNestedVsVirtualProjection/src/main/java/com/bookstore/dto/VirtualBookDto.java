package com.bookstore.dto;

import org.springframework.beans.factory.annotation.Value;

public interface VirtualBookDto {
    
    public String getTitle(); // of book
    public Integer getRank(); // of rank
    
    @Value("#{@authorMapper.buildAuthorDto(target.name, target.genre)}")
    AuthorClassDto getAuthor();
}
