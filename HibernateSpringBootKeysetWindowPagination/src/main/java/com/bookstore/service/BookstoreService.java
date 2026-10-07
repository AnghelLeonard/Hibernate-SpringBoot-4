package com.bookstore.service;

import com.bookstore.dto.KeysetDto;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }    

    public KeysetDto<Author> fetchNextPageOfAuthors(Instant lastCreatedAt, Long lastId, int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"));
        Limit limit = Limit.of(size);

        // re-build the scroll position from classical parameters
        ScrollPosition position;
        if (lastCreatedAt != null && lastId != null) {
            Map<String, Object> keys = new HashMap<>();
            keys.put("createdAt", lastCreatedAt);
            keys.put("id", lastId);

            position = ScrollPosition.forward(keys);
        } else {
            position = ScrollPosition.keyset(); // first page
        }

        Window<Author> window = authorRepository.findBy(position, sort, limit);

        Long nextLastId = null;
        Instant nextLastCreatedAt = null;

        if (window.hasNext() && !window.isEmpty()) {
            
            KeysetScrollPosition ksp = (KeysetScrollPosition) window.positionAt(window.size() - 1);
            nextLastCreatedAt = (Instant) ksp.getKeys().get("createdAt");
            nextLastId = (Long) ksp.getKeys().get("id");
            
            /* or, like this
            Author lastProduct = window.getContent().get(window.size() - 1);
            nextLastId = lastProduct.getId();
            nextLastCreatedAt = lastProduct.getCreatedAt();
            */
        }

        return new KeysetDto<>(window.getContent(), nextLastId, nextLastCreatedAt, window.hasNext());
    }
}
