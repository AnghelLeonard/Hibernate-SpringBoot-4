package com.bookstore.dto;

import java.time.Instant;

public interface AuthorDto { // mirror the Author entity

    public Long getId();    
    public String getName();
    public String getGenre();
    public int getAge();
    public Instant getCreatedAt();
}
