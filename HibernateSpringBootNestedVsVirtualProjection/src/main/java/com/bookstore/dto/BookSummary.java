package com.bookstore.dto;

public interface BookSummary {

    public String getTitle();
    public Integer getRank();
    public AuthorSummary getAuthor();
    
    interface AuthorSummary {
        
        public String getName();
        public String getGenre();
    }
}
