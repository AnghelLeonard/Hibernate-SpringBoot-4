package com.bookstore;

import com.bookstore.service.BookstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    @Autowired
    private BookstoreService bookstoreService;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            System.out.println("\n\npersistAuthorWithBooks():");
            bookstoreService.persistAuthorWithBooks();

            System.out.println("\n\nfetchBooksByAuthorId():");
            bookstoreService.fetchBooksByAuthorId();

            System.out.println("\n\nfetchAllBooks():");
            bookstoreService.fetchAllBooks();

            System.out.println("\n\nfetchBookByTitle():");
            bookstoreService.fetchBookByTitle();

            System.out.println("\n\nfetchBookByIsbn():");
            bookstoreService.fetchBookByIsbn();

            System.out.println("\n\nfetchPaperbackByTitle():");
            bookstoreService.fetchPaperbackByTitle();

            System.out.println("\n\nfetchPaperbackByIsbn():");
            bookstoreService.fetchPaperbackByIsbn();

            System.out.println("\n\nfetchEbookByFormat():");
            bookstoreService.fetchEbookByFormat();

            System.out.println("\n\nfetchAllEbook:");
            bookstoreService.fetchAllEbook();
            
            System.out.println("\n\nfetchEbookByFormatQuery():");
            bookstoreService.fetchEbookByFormatQuery();
            
            System.out.println("\n\nfetchEbooksAndAuthorsJ():");
            bookstoreService.fetchEbooksAndAuthorsJ();

            System.out.println("\n\nfetchEbooksAndAuthorsJF():");
            bookstoreService.fetchEbooksAndAuthorsJF();
            
            System.out.println("\n\nfetchEBookOfTitle():");
            bookstoreService.fetchEBookOfTitle();
            
            System.out.println("\n\nfetchEBooksOfTitleTreat():");
            bookstoreService.fetchEBooksOfTitleTreat();
            
            System.out.println("\n\nfetchEBookOfFormat():");
            bookstoreService.fetchEBookOfFormat();
            
            System.out.println("\n\nfetchEBooksOfFormatTreat():");
            bookstoreService.fetchEBooksOfFormatTreat();
            
            System.out.println("\n\nfetchAuthorAndBooksLazy():");
            bookstoreService.fetchAuthorAndBooksLazy();
            
            System.out.println("\n\nfetchAuthorsWithBooks():");
            bookstoreService.fetchAuthorsWithBooks();
            
            System.out.println("\n\nfetchAuthorsWithEbooksTreat():");
            bookstoreService.fetchAuthorsWithEbooksTreat();
            
            System.out.println("\n\nfetchAuthorsNoBooksTreat():");
            bookstoreService.fetchAuthorsNoBooksTreat();
            
            System.out.println("\n\nfetchAuthorsNoBooksFormatTreat:");
            bookstoreService.fetchAuthorsNoBooksFormatTreat();
            
            System.out.println("\n\nfetchAuthorsNoBooksTitleTreat:");
            bookstoreService.fetchAuthorsNoBooksTitleTreat();
        };
    }
}
