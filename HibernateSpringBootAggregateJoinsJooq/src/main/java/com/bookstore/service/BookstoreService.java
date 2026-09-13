package com.bookstore.service;

import com.bookstore.repository.BookstoreRepository;
import com.bookstore.view.AuthorView;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final BookstoreRepository bookstoreRepository;

    public BookstoreService(BookstoreRepository bookstoreRepository) {
        this.bookstoreRepository = bookstoreRepository;
    }

    public List<AuthorView> fetchAuthorsBooksTagsPublishersReviewersReviews() {

        List<AuthorView> authors = bookstoreRepository.fetchAuthorsBooksTagsPublishersReviewersReviews();

        System.out.println("Authors: " + authors.size());

        return authors;
    }
}
