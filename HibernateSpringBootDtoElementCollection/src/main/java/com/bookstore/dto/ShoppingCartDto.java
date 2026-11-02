package com.bookstore.dto;

public interface ShoppingCartDto {

    public String getOwner();
    public String getTitle();
    public Integer getPrice();    
} 

// or as a Java record
// public record ShoppingCartDto(String owner, String title, Integer price) {}
