package com.bookstore.view;

import java.util.List;

public record AuthorView(
        Long id,
        String name,
        String genre,
        int age,
        List<TagView> tags,
        List<BookView> books        
        ) {}
