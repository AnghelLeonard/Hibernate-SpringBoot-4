package com.bookstore.service;

import com.bookstore.dto.WindowDto;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.util.KeysetTokenEncoder;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final KeysetTokenEncoder tokenEncoder;

    public BookstoreService(AuthorRepository authorRepository, KeysetTokenEncoder tokenEncoder) {
        this.authorRepository = authorRepository;
        this.tokenEncoder = tokenEncoder;
    }    

    public WindowDto<Author> fetchNextPageOfAuthors(String resumeToken, int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"));
        Limit limit = Limit.of(size);

        // decode the token to get the position
        ScrollPosition position = tokenEncoder.decode(resumeToken);

        // query
        Window<Author> window = authorRepository.findBy(position, sort, limit);

        // generate new token
        String nextToken = null;
        if (window.hasNext() && !window.isEmpty()) {
            ScrollPosition lastElementPosition = window.positionAt(window.size() - 1);
            nextToken = tokenEncoder.encode(lastElementPosition);
        }

        return new WindowDto<>(window.getContent(), nextToken, window.hasNext());
    }
}
