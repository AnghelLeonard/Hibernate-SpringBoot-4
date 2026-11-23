package com.bookstore.repository;

import com.bookstore.entity.Chapter;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    @Override    
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)     
    @QueryHints({
        @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")
    })
    public Optional<Chapter> findById(Long id);
    
    public Chapter findByTitle(String title);      
}
