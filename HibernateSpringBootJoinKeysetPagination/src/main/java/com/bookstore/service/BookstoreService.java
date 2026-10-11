package com.bookstore.service;

import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.BookDto;
import com.bookstore.dto.KeysetDto;
import com.bookstore.repository.AuthorRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public KeysetDto<AuthorBookFlatDto> fetchPageOfAuthorsWithBooksByGenreFlatDto(
            Long lastAuthorId, Long lastBookId, int size) {

        List<AuthorBookFlatDto> listOfAuthors = authorRepository.fetchPageOfDto(
                "Anthology", lastAuthorId, lastBookId, Limit.of(size + 1));

        boolean hasMore = false;
        if (listOfAuthors.size() == (size + 1)) {
            hasMore = true;
            listOfAuthors.remove(listOfAuthors.size() - 1);
        }

        Long nextAuthorId = null;
        Long nextBookId = null;

        if (hasMore) {
            nextAuthorId = listOfAuthors.getLast().authorId();
            nextBookId = listOfAuthors.getLast().bookId();
        }

        return new KeysetDto<>(listOfAuthors, nextAuthorId, nextBookId, hasMore);
    }
    
    public KeysetDto<AuthorDto> fetchPageOfAuthorsWithBooksByGenreDto(
            Long lastAuthorId, Long lastBookId, int size) {

        List<Object[]> listOfObjects = authorRepository.fetchPageOfRaw(
                "Anthology", lastAuthorId, lastBookId, Limit.of(size + 1));

        boolean hasMore = false;
        if (listOfObjects.size() == (size + 1)) {
            hasMore = true;
            listOfObjects.remove(listOfObjects.size() - 1);
        }
        
        Map<Long, AuthorDto> authorDtoMap = new LinkedHashMap<>();

        for (Object[] row : listOfObjects) {
            Long authorId = (Long) row[0];
            String authorName = (String) row[1];
            Integer authorAge = (Integer) row[2];
            Long bookId = (Long) row[3];
            String bookTitle = (String) row[4];
            String bookIsbn = (String) row[5];
            
            authorDtoMap.computeIfAbsent(authorId, id -> 
                new AuthorDto(authorId, authorName, authorAge, new ArrayList<>())
            );
           
            if (bookId != null) {
                BookDto bookDto = new BookDto(bookId, bookTitle, bookIsbn);
                authorDtoMap.get(authorId).books().add(bookDto);
            }
        }
       
        List<AuthorDto> listOfAuthors = new ArrayList<>(authorDtoMap.values());

        Long nextAuthorId = null;
        Long nextBookId = null;

        if (hasMore) {
            nextAuthorId = (Long) listOfObjects.getLast()[0];
            nextBookId = (Long) listOfObjects.getLast()[3];
        }

        return new KeysetDto<>(listOfAuthors, nextAuthorId, nextBookId, hasMore);
    }
}
