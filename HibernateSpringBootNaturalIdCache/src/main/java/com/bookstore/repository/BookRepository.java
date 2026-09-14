package com.bookstore.repository;

import com.bookstore.entity.Book;
import com.bookstore.naturalid.NaturalIdRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository<T, ID> extends NaturalIdRepository<Book, Long> {
}
