package com.bookstore.dto;

public interface AuthorBookDto {

    public String getName();  // of author
    public int getAge();      // of author
    public String getTitle(); // of book
    public String getIsbn();  // of book
   
    public Long getTotal();
}
