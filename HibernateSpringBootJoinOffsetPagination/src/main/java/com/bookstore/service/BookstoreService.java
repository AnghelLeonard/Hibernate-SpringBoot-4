package com.bookstore.service;

import com.bookstore.dto.AuthorBookCountFlatDto;
import com.bookstore.dto.AuthorBookDto;
import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.dto.AuthorBookFlatDtoWithCount;
import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.BookDto;
import com.bookstore.repository.AuthorRepository;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Page<AuthorBookFlatDto> fetchPageOfAuthorsWithBooksByGenreDto(int page, int size, Sort sort) {

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AuthorBookFlatDto> pageOfAuthors = authorRepository.fetchPageOfDto("Anthology", pageable);

        return pageOfAuthors;
    }

    public Slice<AuthorBookFlatDto> fetchSliceOfAuthorsWithBooksByGenreDto(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.ASC, "name", "title"));
        Slice<AuthorBookFlatDto> sliceOfAuthors = authorRepository.fetchSliceOfDto("Anthology", pageable);

        return sliceOfAuthors;
    }

    public List<AuthorBookFlatDto> fetchListOfAuthorsWithBookByGenreDto(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.ASC, "name", "title"));
        List<AuthorBookFlatDto> listOfAuthors = authorRepository.fetchListOfDto("Anthology", pageable);

        return listOfAuthors;
    }

    public Page<AuthorBookCountFlatDto> fetchPageOfAuthorsWithBooksByGenreWithCountDto(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.ASC, "name", "title"));

        List<AuthorBookCountFlatDto> listOfAuthors
                = authorRepository.fetchListWithCountOfDto("Anthology", pageable);
        Page<AuthorBookCountFlatDto> pageOfAuthors
                = new PageImpl(listOfAuthors, pageable,
                        listOfAuthors.isEmpty() ? 0 : listOfAuthors.get(0).total());

        return pageOfAuthors;
    }

    public List<AuthorBookFlatDto> fetchListOfAuthorsWithBooksDtoDenseRank(int start, int end) {

        List<AuthorBookFlatDto> listOfAuthors
                = authorRepository.fetchListOfDtoDenseRank("Anthology", start, end);

        return listOfAuthors;
    }

    @Transactional(readOnly = true)
    public AuthorBookFlatDtoWithCount fetchListOfAuthorsWithBooksDtoDenseRankWithCount(int start, int end) {

        List<AuthorBookFlatDto> listOfAuthors
                = authorRepository.fetchListOfDtoDenseRank("Anthology", start, end);
        long total = authorRepository.countByGenre("Anthology");

        AuthorBookFlatDtoWithCount listOfAuthorsWithCount = new AuthorBookFlatDtoWithCount(listOfAuthors, total);

        return listOfAuthorsWithCount;
    }

    public List<AuthorBookFlatDto> fetchListOfAuthorsWithBooksDtoDenseRankNative(int start, int end) {

        List<AuthorBookFlatDto> listOfAuthors
                = authorRepository.fetchListOfDtoDenseRankNative("Anthology", start, end);

        return listOfAuthors;
    }

    @Transactional(readOnly = true)
    public Map.Entry<List<Object[]>, Long> fetchHierarchyOfAuthorsWithBooksDtoDenseRankWithCount(int start, int end) {

        List<Object[]> listOfAuthors
                = authorRepository.fetchListOfRawDenseRank("Anthology", start, end);
        long total = authorRepository.countByGenre("Anthology");

        return new AbstractMap.SimpleImmutableEntry<>(listOfAuthors, total);
    }

    public AuthorBookDto mapToDto(List<Object[]> listOfAuthors, Long total) {

        Map<Long, AuthorDto> authorDtoMap = new LinkedHashMap<>();

        for (Object[] row : listOfAuthors) {
            Long authorId = (Long) row[0];
            String authorName = (String) row[1];
            Integer authorAge = (Integer) row[2];
            Long bookId = (Long) row[3];
            String bookTitle = (String) row[4];
            String bookIsbn = (String) row[5];

            authorDtoMap.computeIfAbsent(authorId, id
                    -> new AuthorDto(authorId, authorName, authorAge, new ArrayList<>())
            );

            if (bookId != null) {
                BookDto bookDto = new BookDto(bookId, bookTitle, bookIsbn);
                authorDtoMap.get(authorId).books().add(bookDto);
            }
        }

        List<AuthorDto> content = new ArrayList<>(authorDtoMap.values());

        return new AuthorBookDto(content, total);
    }

    public AuthorBookDto fetchHierarchyOfAuthorsWithBooksDtoDenseRankWithCountCTE(int start, int end) {

        List<Object[]> listOfAuthors
                = authorRepository.fetchListOfRawDenseRankWithCount("Anthology", start, end);

        Map<Long, AuthorDto> authorDtoMap = new LinkedHashMap<>();

        Long total = null;
        for (Object[] row : listOfAuthors) {
            Long authorId = (Long) row[0];
            String authorName = (String) row[1];
            Integer authorAge = (Integer) row[2];
            Long bookId = (Long) row[3];
            String bookTitle = (String) row[4];
            String bookIsbn = (String) row[5];
            total = (Long) row[6];

            authorDtoMap.computeIfAbsent(authorId, id
                    -> new AuthorDto(authorId, authorName, authorAge, new ArrayList<>())
            );

            if (bookId != null) {
                BookDto bookDto = new BookDto(bookId, bookTitle, bookIsbn);
                authorDtoMap.get(authorId).books().add(bookDto);
            }
        }

        List<AuthorDto> content = new ArrayList<>(authorDtoMap.values());

        return new AuthorBookDto(content, total);
    }
}
