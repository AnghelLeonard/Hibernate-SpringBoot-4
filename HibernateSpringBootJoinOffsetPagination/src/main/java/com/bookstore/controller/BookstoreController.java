package com.bookstore.controller;

import com.bookstore.dto.AuthorBookCountFlatDto;
import com.bookstore.dto.AuthorBookDto;
import com.bookstore.dto.AuthorBookFlatDto;
import com.bookstore.dto.AuthorBookFlatDtoWithCount;
import com.bookstore.dto.AuthorDto;
import com.bookstore.service.BookstoreService;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import static org.springframework.data.domain.Sort.sort;
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
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "1") int end) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoDenseRank(start, end));
    }
    
    @GetMapping("/authorsAndBooksDenseRankWithCount")
    public ResponseEntity<AuthorBookFlatDtoWithCount> fetchListOfAuthorsWithBooksDtoDenseRankWithCount(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "1") int end) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoDenseRankWithCount(start, end));
    }

    @GetMapping("/authorsAndBooksDenseRankNative")
    public ResponseEntity<List<AuthorBookFlatDto>> fetchListOfAuthorsWithBooksDtoDenseRankNative(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "1") int end) {

        return ResponseEntity.ok(bookstoreService.fetchListOfAuthorsWithBooksDtoDenseRankNative(start, end));
    }
    
    @GetMapping("/authorsAndBooksHierarchy")
    public ResponseEntity<AuthorBookDto> fetchPageOfAuthorsWithBooksByGenreHierarchy(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "1") int end) {
        
        Map.Entry<List<Object[]>, Long> result = bookstoreService
                .fetchHierarchyOfAuthorsWithBooksDtoDenseRankWithCount(start, end);

        return ResponseEntity.ok(bookstoreService.mapToDto(result.getKey(), result.getValue()));
    }
    
    @GetMapping("/authorsAndBooksHierarchyCTE")
    public ResponseEntity<AuthorBookDto> fetchPageOfAuthorsWithBooksByGenreHierarchyCTE(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "1") int end) {              

        return ResponseEntity.ok(
                bookstoreService.fetchHierarchyOfAuthorsWithBooksDtoDenseRankWithCountCTE(start, end));
    }
}
