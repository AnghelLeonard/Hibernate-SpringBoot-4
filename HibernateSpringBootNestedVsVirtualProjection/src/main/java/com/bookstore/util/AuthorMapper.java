package com.bookstore.util;

import com.bookstore.dto.AuthorClassSummary;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    
    public AuthorClassSummary buildAuthorSummary(String genre, String name) {
        
        AuthorClassSummary authorClassSummary = new AuthorClassSummary();
        authorClassSummary.setName(name);
        authorClassSummary.setGenre(genre);
        
        return authorClassSummary;
    }
}
