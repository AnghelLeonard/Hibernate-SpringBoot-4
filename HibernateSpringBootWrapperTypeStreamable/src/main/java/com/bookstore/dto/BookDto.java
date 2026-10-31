package com.bookstore.dto;

public record BookDto(Integer price, String title) {}

/*
public class BookDto {

    private final Integer price;
    private final String title;

    public BookDto(Integer price, String title) {
        this.price = price;
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }       

    @Override
    public String toString() {
        return "BookDto{" + "price=" + price + ", title=" + title + '}';
    }        
}
*/
