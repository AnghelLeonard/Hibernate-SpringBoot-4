package com.bookstore.projection;

public interface AuthorNameBookTitle {
            
    String getName();    
    String getTitle();    
}

// or, Java record
// public record AuthorNameBookTitle(String name, String title) {}
