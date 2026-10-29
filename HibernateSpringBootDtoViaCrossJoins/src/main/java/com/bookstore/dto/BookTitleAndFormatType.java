package com.bookstore.dto;

public interface BookTitleAndFormatType {
                
    String getTitle();        
    String getFormatType();
}

// or Java record
// public record BookTitleAndFormatType(String title, String formatType) {}
