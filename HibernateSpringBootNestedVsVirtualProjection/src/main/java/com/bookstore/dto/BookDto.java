package com.bookstore.dto;

public interface BookDto {

    public String getTitle();
    public Integer getRank();
    public AuthorDto getAuthor();
    
    interface AuthorDto {
        
        public String getName();
        public String getGenre();
    }
}
