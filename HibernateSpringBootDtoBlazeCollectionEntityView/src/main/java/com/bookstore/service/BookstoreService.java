package com.bookstore.service;

import com.bookstore.view.AuthorBookView;
import com.bookstore.view.AuthorBookViewRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorBookViewRepository authorBookViewRepository;

    public BookstoreService(AuthorBookViewRepository authorBookViewRepository) {

        this.authorBookViewRepository = authorBookViewRepository;
    }

    public void fetchAuthorBookView() {

        List<AuthorBookView> rs = (List<AuthorBookView>) authorBookViewRepository.findAll();

        for (AuthorBookView a : rs) {
            System.out.println("Author | Id: " + a.getId() + ", Name:" + a.getName() + ", Age: " + a.getAge());
            a.getBooks().forEach(b -> System.out.println("Book | Id: " + b.getId() + ", Title: " + b.getTitle()));
        }
    }
}
