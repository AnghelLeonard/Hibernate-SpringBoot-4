package com.bookstore.controller;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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

    // http://localhost:8080/authors
    // http://localhost:8080/authors?page=1&size=3
    // http://localhost:8080/authors?page=1&size=3&sort=name,desc
    // http://localhost:8080/authors?page=1&size=3&sort=name,desc&sort=genre,asc
    @GetMapping("/authors")
    public ResponseEntity<Page<Author>> fetchAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @SortDefault(sort = "age", direction = Sort.Direction.DESC) Sort sort
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPage(page, size, sort));
        // return only content without metadata
        // return ResponseEntity.ok(bookstoreService.fetchNextPage(page, size, sort).getContent());
    }

    // http://localhost:8080/authorsByGenre
    // http://localhost:8080/authorsByGenre?page=1
    // http://localhost:8080/authorsByGenre?size=3
    // http://localhost:8080/authorsByGenre?page=1&size=3
    @GetMapping("/authorsByGenre")
    public ResponseEntity<Page<Author>> fetchAuthorsByGenre(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageByGenre(page, size));
    }

    @GetMapping("/authorsByGenreExplicitCount")
    public ResponseEntity<Page<Author>> fetchAuthorsByGenreExplicitCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageByGenreExplicitCount(page, size));
    }

    @GetMapping("/authorsByGenreNative")
    public ResponseEntity<Page<Author>> fetchAuthorsByGenreNative(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageByGenreNative(page, size));
    }

    @GetMapping("/authorsByGenreNativeExplicitCount")
    public ResponseEntity<Page<Author>> fetchAuthorsByGenreNativeExplicitCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageByGenreNativeExplicitCount(page, size));
    }

    @GetMapping("/authors1")
    // http://localhost:8080/authors1?page=1&size=3&sort=name,desc
    public Page<Author> fetchAuthors1(Pageable pageable) {

        return bookstoreService.fetchNextPagePageable(pageable);
    }

    @GetMapping("/authors2")
    // http://localhost:8080/authors2?page=1&size=3&sort=id,asc&sort=name,desc
    public Page<Author> fetchAuthors2(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
        @SortDefault(sort = "age", direction = Sort.Direction.ASC),
        @SortDefault(sort = "genre", direction = Sort.Direction.DESC)
    }) Pageable pageable) {
        return bookstoreService.fetchNextPagePageable(pageable);
    }  

    // http://localhost:8080/authorsByGenreDto
    // http://localhost:8080/authorsByGenreDto?page=1
    // http://localhost:8080/authorsByGenreDto?size=3
    // http://localhost:8080/authorsByGenreDto?page=1&size=3
    @GetMapping("/authorsByGenreDto")
    public ResponseEntity<Page<AuthorDto>> fetchAuthorsByGenreDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageByGenreDto(page, size));
    }

    @GetMapping("/authorsByGenreExplicitCountDto")
    public ResponseEntity<Page<AuthorDto>> fetchAuthorsByGenreExplicitCountDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageByGenreExplicitCountDto(page, size));
    }

    @GetMapping("/authorsByGenreNativeDto")
    public ResponseEntity<Page<AuthorDto>> fetchAuthorsByGenreNativeDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageByGenreNativeDto(page, size));
    }

    @GetMapping("/authorsByGenreNativeExplicitCountDto")
    public ResponseEntity<Page<AuthorDto>> fetchAuthorsByGenreNativeExplicitCountDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(bookstoreService.fetchNextPageByGenreNativeExplicitCountDto(page, size));
    }
}
