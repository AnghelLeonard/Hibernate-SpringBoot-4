package com.bookstore;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.BookDto;
import com.bookstore.service.BookstoreService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);
    
    private final BookstoreService bookstoreService;

    public MainApplication(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            // example 1, complex bracketed filter + join + DTO projection
            log.info("--- 1. Fetching authors with complex filter ---");
            List<AuthorDto> complexAuthors = bookstoreService.fetchAuthorsWithComplexFilter();
            complexAuthors.forEach(author -> log.info("Author: {}", author));

            // example 2, paginated BETWEEN filter + DTO projection
            log.info("--- 2. Fetching books page (price between 25 and 60) ---");
            Page<BookDto> booksPage = bookstoreService.fetchBooksPage(0, 5);
            log.info("Total books matching: {}", booksPage.getTotalElements());
            booksPage.forEach(book -> log.info("Book: {}", book));

            // example 3, dynamic URL-style filter string for Authors (with brackets & join)
            log.info("--- 3. Searching authors via filter expression ---");
            String authorFilter = "(age:gt:40 AND genre:eq:Anthology) OR (books.price:lt:30)";
            List<AuthorDto> searchedAuthors = bookstoreService.searchAuthors(authorFilter);
            searchedAuthors.forEach(author -> log.info("Matched Author: {}", author));

            // example 4, dynamic URL-style filter string for Books (IN + BETWEEN + join)
            log.info("--- 4. Searching books via filter expression ---");
            String bookFilter = "price:between:25|50 AND author.genre:in:Sci-Fi|History";
            Page<BookDto> searchedBooks = bookstoreService.searchBooks(
                    bookFilter, PageRequest.of(0, 10, Sort.by("title").ascending()));
            searchedBooks.forEach(book -> log.info("Matched Book: {}", book));
        };
    }
}
