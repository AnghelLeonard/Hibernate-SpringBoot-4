package com.bookstore.service;

import com.bookstore.dto.AuthorContributionDto;
import com.bookstore.dto.BookCatalogDto;
import com.bookstore.entity.Book;
import com.bookstore.entity.BookAuthor;
import com.bookstore.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;

    public BookstoreService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookCatalogDto> getPublishedCatalog() {
        // Initial query via findByStatus(), SELECT * FROM "book" WHERE "status" = 'PUBLISHED'
        // Triggers TRAP 1 (Publisher EAGER) and TRAP 2 (RoyaltyContract non-owning OneToOne)
        List<Book> books = bookRepository.findByStatus("PUBLISHED");

        return books.stream()
                .map(this::mapToCatalogDto)
                .toList();
    }

    private BookCatalogDto mapToCatalogDto(Book book) {
        // Already populated during findByStatus() via Trap 1 and Trap 2
        String publisherName = book.getPublisher().getName();
        String contractCode = (book.getRoyaltyContract() != null)
                ? book.getRoyaltyContract().getContractCode()
                : "NO_CONTRACT";

        // Triggers TRAP 3 since lazy @OneToMany collection is initialized per Book
        List<AuthorContributionDto> authorDtos = book.getBookAuthors().stream()
                .map(this::mapToAuthorDto)
                .toList();

        return new BookCatalogDto(
                book.getIsbn(),
                publisherName,
                contractCode,
                authorDtos
        );
    }

    private AuthorContributionDto mapToAuthorDto(BookAuthor bookAuthor) {
        // Triggers TRAP 4 since lazy @ManyToOne is initialized per BookAuthor row
        // Subsequently triggers TRAP 5 when author.getAgency().getName() is called
        var author = bookAuthor.getAuthor();

        return new AuthorContributionDto(
                author.getPenName(),
                bookAuthor.getRole(),
                bookAuthor.getRoyaltySplit(),
                author.getAgency().getName()
        );
    }
}