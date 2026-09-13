package com.bookstore.view;

import java.util.List;

public record ReviewView(
        Long id,
        String script,
        String language,
        List<ReviewerView> reviewers) {}
