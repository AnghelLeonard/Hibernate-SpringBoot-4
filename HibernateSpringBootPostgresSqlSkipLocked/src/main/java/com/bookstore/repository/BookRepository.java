package com.bookstore.repository;

import java.util.List;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.Timeouts;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
        @QueryHint(name = "jakarta.persistence.lock.timeout", value = "" + Timeouts.SKIP_LOCKED_MILLI)})
    public List<Book> findTop3ByStatus(BookStatus status, Sort sort);
}
