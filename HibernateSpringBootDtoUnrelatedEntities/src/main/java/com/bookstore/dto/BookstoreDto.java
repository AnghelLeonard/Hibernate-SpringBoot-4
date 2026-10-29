package com.bookstore.dto;

public interface BookstoreDto {
            
    String getName();
    
    String getTitle();    
}

// or, a Java record
// public record BookstoreDto(String name, String title) {}
