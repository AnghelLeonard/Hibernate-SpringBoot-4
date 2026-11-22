package com.bookstore.repository;

import com.bookstore.entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
}
