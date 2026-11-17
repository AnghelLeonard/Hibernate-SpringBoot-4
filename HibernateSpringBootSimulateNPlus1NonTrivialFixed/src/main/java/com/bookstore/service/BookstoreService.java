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
        
        List<Book> books = bookRepository.findPublishedWithFullGraph("PUBLISHED");

        return books.stream()
                .map(this::mapToCatalogDto)
                .toList();
    }

    private BookCatalogDto mapToCatalogDto(Book book) {
        
        String publisherName = book.getPublisher().getName();
        String contractCode = (book.getRoyaltyContract() != null)
                ? book.getRoyaltyContract().getContractCode()
                : "NO_CONTRACT";
        
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
      
        var author = bookAuthor.getAuthor();

        return new AuthorContributionDto(
                author.getPenName(),
                bookAuthor.getRole(),
                bookAuthor.getRoyaltySplit(),
                author.getAgency().getName()
        );
    }
}