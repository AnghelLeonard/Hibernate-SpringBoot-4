package com.bookstore.service;

import com.bookstore.dto.WindowResponse;
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

    public WindowResponse<Author> fetchNextPageOfAuthors(String resumeToken, int size) {

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

        return new WindowResponse<>(window.getContent(), nextToken, window.hasNext());
    }
}
