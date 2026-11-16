package com.bookstore.controller;

import com.bookstore.dto.AuthorBookCountFlatDto;
import com.bookstore.dto.AuthorBookDto;
import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.dto.AuthorBookFlatDtoKeyset;
import com.bookstore.service.BookstoreService;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookstoreController {

    private final BookstoreService bookstoreService;

    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/authorsAndBooks")
    public ResponseEntity<Page<AuthorBookFlatDto>> fetchPageOfAuthorsWithBooksByGenreDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @SortDefaults({
        @SortDefault(sort = "name", direction = Sort.Direction.ASC),
        @SortDefault(sort = "title", direction = Sort.Direction.ASC)
    }) Sort sort) {

        return ResponseEntity.ok(bookstoreService.fetchPageOfAuthorsWithBooksByGenreDto(page, size, sort));
    }

    @GetMapping("/authorsAndBooksSlice")
    public ResponseEntity<Slice<AuthorBookFlatDto>> fetchSliceOfAuthorsWithBooksByGenreDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchSliceOfAuthorsWithBooksByGenreDto(page, size));
    }

    @GetMapping("/authorsAndBooksList")
    public ResponseEntity<List<AuthorBookFlatDto>> fetchListOfAuthorsWithBookByGenreDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBookByGenreDto(page, size));
    }

    @GetMapping("/authorsAndBooksCount")
    public ResponseEntity<Page<AuthorBookCountFlatDto>> fetchPageOfAuthorsWithBooksByGenreWithCountDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(bookstoreService.fetchPageOfAuthorsWithBooksByGenreWithCountDto(page, size));
    }

    @GetMapping("/authorsAndBooksDenseRank")
    public ResponseEntity<List<AuthorBookFlatDto>> fetchListOfAuthorsWithBooksDtoDenseRank(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoDenseRank(offset, size));
    }

    @GetMapping("/authorsAndBooksDenseRankNative")
    public ResponseEntity<List<AuthorBookFlatDto>> fetchListOfAuthorsWithBooksDtoDenseRankNative(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoDenseRankNative(offset, size));
    }

    @GetMapping("/authorsAndBooksExists")
    public ResponseEntity<List<AuthorBookFlatDto>> fetchListOfAuthorsWithBooksDtoExists(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoExists(offset, size));
    }

    @GetMapping("/authorsAndBooksExistsNative")
    public ResponseEntity<List<AuthorBookFlatDto>> fetchListOfAuthorsWithBooksDtoExistsNative(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoExistsNative(offset, size));
    }

    @GetMapping("/authorsAndBooksExists2Queries")
    public ResponseEntity<List<AuthorBookFlatDto>> fetchListOfAuthorsWithBooksDtoExistsTwoQueries(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoExistsTwoQueries(offset, size));
    }

    @GetMapping("/authorsAndBooksKeyset")
    public ResponseEntity<List<AuthorBookFlatDtoKeyset>> fetchListOfAuthorsWithBooksDtoKeyset(
            @RequestParam(required = false) Instant lastCreatedAt,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoKeyset(
                lastCreatedAt, lastId, size));
    }

    @GetMapping("/authorsAndBooksDenseRankTotal")
    public ResponseEntity<List<Object[]>> fetchDenseRankedAndTotal(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchDenseRankedAndTotal(offset, size));
    }
    
    @GetMapping("/authorsAndBooksRowNumberTotal")
    public ResponseEntity<List<Object[]>> fetchRowNumberAndTotal(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchRowNumberAndTotal(offset, size));
    }
    
    @GetMapping("/hierarchyOfAuthorsAndBooksDenseRankTotal")
    public ResponseEntity<AuthorBookDto> fetchHierarchyDenseRankedAndTotal(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchHierarchyDenseRankedAndTotal(offset, size));
    }
        
    @GetMapping("/hierarchyOfAuthorsAndBooksRowNumberTotal")
    public ResponseEntity<AuthorBookDto> fetchHierarchyRowNumberAndTotal(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "1") int size) {

        return ResponseEntity.ok(bookstoreService.fetchHierarchyRowNumberAndTotal(offset, size));
    }
}
