package com.bookstore.dto;

public record BookAuthorSearchCriteria(String titlePattern, String authorName, boolean sortByTitle) {}
