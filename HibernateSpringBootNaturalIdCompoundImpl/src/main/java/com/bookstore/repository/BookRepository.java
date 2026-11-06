package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.stereotype.Repository;
import com.bookstore.naturalid.NaturalIdRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BookRepository<T, ID> extends NaturalIdRepository<Book, Long> {
}
