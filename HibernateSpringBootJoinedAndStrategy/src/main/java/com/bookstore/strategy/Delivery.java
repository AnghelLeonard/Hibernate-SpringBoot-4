package com.bookstore.strategy;

import com.bookstore.entity.Book;

public interface Delivery<T extends Book> {

    Class<T> ofBook();    
    void deliver(T book);
}
