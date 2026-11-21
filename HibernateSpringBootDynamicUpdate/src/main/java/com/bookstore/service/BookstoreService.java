package com.bookstore.service;

import com.bookstore.entity.Author;
import org.springframework.stereotype.Service;
import com.bookstore.repository.AuthorRepository;
import java.time.Instant;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
     // Scenario 1: 
     // High-frequency operational update (HOT-update friendly). Only 'sell_rank' and 'last_active_at' are modified.
     // Hibernate skips sending the heavy 'biography' (TEXT) and 'social_metadata' (JSONB) columns,
     // and leaves all indexed columns untouched so PostgreSQL can perform a HOT update.     
    @Transactional
    public void updateAuthorSellRank(Long authorId, int newSellRank) {
        Author author = authorRepository.findById(authorId).orElseThrow();

        author.setSellRank(newSellRank);
        author.setLastActiveAt(Instant.now());
    }
    
     // Scenario 2: 
     // Recording a book sale. Increments 'total_books_sold' without touching profile or JSONB columns.    
    @Transactional
    public void recordBookSale(Long authorId, long copiesSold) {
        Author author = authorRepository.findById(authorId).orElseThrow();

        long currentTotal = author.getTotalBooksSold() != null ? author.getTotalBooksSold() : 0L;
        author.setTotalBooksSold(currentTotal + copiesSold);
        author.setLastActiveAt(Instant.now());
    }
   
     // Scenario 3: 
     // Profile/Biography edit (demonstrates versionless dirty locking).
     // Can run concurrently with updateAuthorSellRank() on the exact same Author row
     // without throwing OptimisticLockException because different columns are dirty.     
    @Transactional
    public void updateAuthorBiography(Long authorId, String newBiography, Map<String, Object> updatedSocial) {
        Author author = authorRepository.findById(authorId).orElseThrow();

        author.setBiography(newBiography);
        author.setSocialMetadata(updatedSocial);
    }
}
