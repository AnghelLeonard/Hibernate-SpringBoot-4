package com.bookstore.view;

import java.util.List;

public record BookView(
        Long id,
        String title,
        String isbn,               
        List<ReviewView> reviews,
        PublisherView publisher // this is many-to-one
        ) {}
