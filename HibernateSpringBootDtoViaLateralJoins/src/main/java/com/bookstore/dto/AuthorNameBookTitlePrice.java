package com.bookstore.dto;

public interface AuthorNameBookTitlePrice {
            
    String getName();    
    String getTitle();    
    Integer getPrice();
}

// or Java record
// public record AuthorNameBookTitlePrice(String name, String title, Integer price) {}
