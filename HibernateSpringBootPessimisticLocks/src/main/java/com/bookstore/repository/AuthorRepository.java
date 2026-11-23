package com.bookstore.repository;

import com.bookstore.entity.Author;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ) // for exclusive lock switch to PESSIMISTIC_WRITE
    public Optional<Author> findById(Long id);
}
