package com.bookstore.dto;

public interface AuthorNameAgeDefaults {
    
    // matches the Domain Model exactly
    String getName();
    
    Integer getAge(); // exposed so it can be used inside the default method

    // Replaces: @Value("#{target.age}")
    default String years() {
        return String.valueOf(getAge());
    }

    // Replaces: @Value("#{ T(java.lang.Math).random() * 10000 }")
    default int rank() {
        return (int) (Math.random() * 10000);
    }

    // Replaces: @Value("5")
    default String books() {
        return "5";
    }
}
