package com.bookstore.service;

import static com.bookstore.builder.Condition.LogicalOperatorType.AND;
import static com.bookstore.builder.Condition.LogicalOperatorType.END;
import static com.bookstore.builder.Condition.LogicalOperatorType.OR;
import static com.bookstore.builder.Condition.OperationType.BETWEEN;
import static com.bookstore.builder.Condition.OperationType.EQUAL;
import static com.bookstore.builder.Condition.OperationType.GREATER_THAN;
import static com.bookstore.builder.Condition.OperationType.LESS_THAN;
import com.bookstore.builder.SpecificationBuilder;
import com.bookstore.builder.SpecificationParser;
import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.BookDto;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private static final Logger log = LoggerFactory.getLogger(BookstoreService.class);

    private static final Set<String> ALLOWED_AUTHOR_FIELDS = Set.of(
            "id", "age", "name", "genre", "rating", "books.title", "books.price", "books.isbn"
    );

    private static final Set<String> ALLOWED_BOOK_FIELDS = Set.of(
            "id", "name", "title", "isbn", "price", "author.name", "author.genre", "author.age"
    );

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final SpecificationParser specificationParser;

    public BookstoreService(AuthorRepository authorRepository,
            BookRepository bookRepository,
            SpecificationParser specificationParser) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.specificationParser = specificationParser;
    }

    // example 1, programmatic complex brackets + join + DTO projection
    public List<AuthorDto> fetchAuthorsWithComplexFilter() {
        SpecificationBuilder<Author> specBuilder = new SpecificationBuilder<>();

        // SQL: WHERE (age > 40 AND genre = 'Anthology') OR (rating = 5 AND books.price < 55)
        Specification<Author> specAuthor = specBuilder
                .withGroup(g -> g
                .with("age", 40, GREATER_THAN, AND)
                .with("genre", "Anthology", EQUAL, END), OR)
                .withGroup(g -> g
                .with("rating", 5, EQUAL, AND)
                .with("books.price", 55, LESS_THAN, END), END)
                .build();

        List<AuthorDto> authors = authorRepository.findBy(
                specAuthor, q -> q.as(AuthorDto.class).all());

        log.info("Fetched {} AuthorDtos", authors.size());
        return authors;
    }

    // example 2, programmatic BETWEEN + Pageable + DTO projection
    public Page<BookDto> fetchBooksPage(int page, int size) {
        SpecificationBuilder<Book> specBuilder = new SpecificationBuilder<>();

        Specification<Book> specBook = specBuilder
                .withValues("price", List.of(25, 60), BETWEEN, END)
                .build();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "title"));

        Page<BookDto> books = bookRepository.findBy(
                specBook, q -> q.as(BookDto.class).page(pageable));

        log.info("Fetched page {} of BookDtos (total: {})", page, books.getTotalElements());
        return books;
    }

    // example 3, dynamic URL query parameter search returning DTOs
    public List<AuthorDto> searchAuthors(String filter) {
        Specification<Author> spec = specificationParser.parse(filter, ALLOWED_AUTHOR_FIELDS);
        return authorRepository.findBy(spec, q -> q.as(AuthorDto.class).all());
    }

    // example 4, dynamic URL-style filter string for Books (IN + BETWEEN + join)
    public Page<BookDto> searchBooks(String filter, Pageable pageable) {
        Specification<Book> spec = specificationParser.parse(filter, ALLOWED_BOOK_FIELDS);
        return bookRepository.findBy(spec, q -> q.as(BookDto.class).page(pageable));
    }
}
