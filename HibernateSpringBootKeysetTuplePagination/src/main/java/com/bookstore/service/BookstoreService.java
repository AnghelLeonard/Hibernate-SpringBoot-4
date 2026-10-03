package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.KeysetDto;
import com.bookstore.repository.AuthorRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> fetchNextPageOfAuthors(Instant ltime, Long lid, int size) {
        return authorRepository.fetchNextPage(ltime, lid, size);
    }

    public KeysetDto<AuthorDto> fetchNextPageOfAuthorsWithMetadata(
            Instant lastCreatedAt, Long lastId, int size) {

        Instant newLastCreatedAt = null;
        Long newLastId = null;
        boolean hasNext = false;

        List<AuthorDto> authors = authorRepository.fetchNextPage(lastCreatedAt, lastId, (size + 1));

        if (!authors.isEmpty()) {
            hasNext = authors.size() == (size + 1);

            if (hasNext) {
                authors.remove(authors.size() - 1);
            }

            AuthorDto lastAuthor = authors.get(authors.size() - 1);
            newLastCreatedAt = lastAuthor.getCreatedAt();
            newLastId = lastAuthor.getId();
        }

        return new KeysetDto<>(authors, newLastCreatedAt, newLastId, hasNext);
    }
}
