package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.stereotype.Repository;
import com.bookstore.naturalid.NaturalIdRepository;

@Repository
public interface BookRepository<T, ID> extends NaturalIdRepository<Book, Long> {
}
