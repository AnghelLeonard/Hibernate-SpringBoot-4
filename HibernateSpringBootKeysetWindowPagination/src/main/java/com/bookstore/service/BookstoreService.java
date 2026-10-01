package com.bookstore.service;

import com.bookstore.dto.KeysetPageResponse;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
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

    public void insertData() {

        for (int i = 0; i < 100; i++) {

            Author author = new Author();

            author.setAge(0);
            author.setGenre("Genre-" + i);
            author.setName("Name-" + i);

            authorRepository.save(author);
        }

        System.out.println("Done inserting ... try 'localhost:8080/authors'");
    }

    public KeysetPageResponse<Author> fetchNextPageOfAuthors(Instant lastCreatedAt, Long lastId, int size) {

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
            Author lastProduct = window.getContent().get(window.size() - 1);
            nextLastId = lastProduct.getId();
            nextLastCreatedAt = lastProduct.getCreatedAt();
        }

        return new KeysetPageResponse<>(window.getContent(), nextLastId, nextLastCreatedAt, window.hasNext());
    }
}
