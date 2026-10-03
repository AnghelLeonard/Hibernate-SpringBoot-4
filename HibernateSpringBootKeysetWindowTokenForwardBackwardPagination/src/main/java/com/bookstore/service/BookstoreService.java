package com.bookstore.service;

import com.bookstore.dto.PagedResponse;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.util.TokenSerializer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public PagedResponse<Author> fetchNextPageOfAuthors(int size, String token, String direction) {

        ScrollPosition position;
        boolean isBackward = "backward".equalsIgnoreCase(direction);

        if (token == null || token.isBlank()) {
            position = ScrollPosition.keyset();
        } else {
            Map<String, Object> keys = TokenSerializer.deserialize(token);
            if (isBackward) {
                position = ScrollPosition.backward(keys);
            } else {
                position = ScrollPosition.forward(keys);
            }
        }

        // Fetch data window from DB
        Window<Author> window = authorRepository.findByOrderByCreatedAtDescIdDesc(position, Limit.of(size));
        List<Author> content = window.getContent();

        if (content.isEmpty()) {
            return new PagedResponse<>(Collections.emptyList(), null, false);
        }

        boolean hasMore = window.hasNext();
        String nextToken = null;

        if (hasMore) {
            int targetIndex = isBackward ? 0 : content.size() - 1;

            KeysetScrollPosition nextPosition = (KeysetScrollPosition) window.positionAt(targetIndex);
            nextToken = TokenSerializer.serialize(nextPosition.getKeys());
        }
        return new PagedResponse<>(content, nextToken, window.hasNext());
    }
}
