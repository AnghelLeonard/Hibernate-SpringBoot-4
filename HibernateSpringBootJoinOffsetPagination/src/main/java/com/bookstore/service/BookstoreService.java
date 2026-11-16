package com.bookstore.service;

import com.bookstore.dto.AuthorBookCountFlatDto;
import com.bookstore.dto.AuthorBookDto;
import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.dto.AuthorBookFlatDtoKeyset;
import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.BookDto;
import com.bookstore.repository.AuthorRepository;
import java.time.Instant;
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

    public List<AuthorBookFlatDto> fetchListOfAuthorsWithBooksDtoDenseRank(int offset, int size) {

        List<AuthorBookFlatDto> listOfAuthors
                = authorRepository.fetchListOfDtoDenseRank("Anthology", offset, size);

        return listOfAuthors;
    }

    public List<AuthorBookFlatDto> fetchListOfAuthorsWithBooksDtoDenseRankNative(int offset, int size) {

        List<AuthorBookFlatDto> listOfAuthors
                = authorRepository.fetchListOfDtoDenseRankNative("Anthology", offset, size);

        return listOfAuthors;
    }

    public List<AuthorBookFlatDto> fetchListOfAuthorsWithBooksDtoExists(int offset, int size) {

        List<AuthorBookFlatDto> listOfAuthors
                = authorRepository.fetchListOfDtoExists("Anthology", offset, size);

        return listOfAuthors;
    }

    public List<AuthorBookFlatDto> fetchListOfAuthorsWithBooksDtoExistsNative(int offset, int size) {

        List<AuthorBookFlatDto> listOfAuthors
                = authorRepository.fetchListOfDtoExistsNative("Anthology", offset, size);

        return listOfAuthors;
    }

    @Transactional(readOnly = true)
    public List<AuthorBookFlatDto> fetchListOfAuthorsWithBooksDtoExistsTwoQueries(int offset, int size) {

        List<Long> ids = authorRepository.fetchListOfDtoExistsIds("Anthology", offset, size);
        List<AuthorBookFlatDto> listOfAuthors
                = authorRepository.fetchListOfDtoExistsBooks(ids);

        return listOfAuthors;
    }

    public List<AuthorBookFlatDtoKeyset> fetchListOfAuthorsWithBooksDtoKeyset(
            Instant lastCreatedAt, Long lastId, int size) {

        List<AuthorBookFlatDtoKeyset> listOfAuthors = (lastCreatedAt == null || lastId == null)
                ? authorRepository.fetchFirstListOfDtoKeyset("Anthology", size)
                : authorRepository.fetchNextListOfDtoKeyset("Anthology", lastCreatedAt, lastId, size);

        return listOfAuthors;
    }

    public List<Object[]> fetchDenseRankedAndTotal(int offset, int size) {

        List<Object[]> listOfAuthors
                = authorRepository.fetchDenseRankedAndTotal("Anthology", offset, size);

        return listOfAuthors;
    }

    public List<Object[]> fetchRowNumberAndTotal(int offset, int size) {

        List<Object[]> listOfAuthors
                = authorRepository.fetchRowNumberAndTotal("Anthology", offset, size);

        return listOfAuthors;
    }

    public AuthorBookDto fetchHierarchyDenseRankedAndTotal(int offset, int size) {

        List<Object[]> listOfAuthors
                = authorRepository.fetchDenseRankedAndTotal("Anthology", offset, size);

        return mapToDto(listOfAuthors);
    }

    public AuthorBookDto fetchHierarchyRowNumberAndTotal(int offset, int size) {

        List<Object[]> listOfAuthors
                = authorRepository.fetchRowNumberAndTotal("Anthology", offset, size);

        return mapToDto(listOfAuthors);
    }

    private AuthorBookDto mapToDto(List<Object[]> listOfAuthors) {

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
