package com.bookstore.dto;

import java.time.Instant;

public interface AuthorDto {

    public Long getId();
    public int getAge();
    public Instant getCreatedAt();
    public String getName();
    public String getGenre();    
}
