package com.bookstore;

import com.bookstore.dto.PurgeDto;
import com.bookstore.service.BookstoreService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {
    
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
           System.out.println("Total updated rows (callPurgeAuthorsJt()): " + bookstoreService.callPurgeAuthorsJt());
           System.out.println("Total updated rows (callMigrateGenreInBatchesJt()): " + bookstoreService.callMigrateGenreInBatchesJt());
           System.out.println("Total updated rows (runGenreMigration()): " + bookstoreService.runGenreMigration());
           System.out.println("Total updated rows (runGenreMigrationProcedure()): " + bookstoreService.runGenreMigrationProcedure());
           System.out.println("Results (runPurgeAuthorsByAgeInBatches()): ");
           PurgeDto pd = bookstoreService.runPurgeAuthorsByAgeInBatches();
           System.out.println("Deleted authors: " + pd.getDeletedAuthors());
           System.out.println("Deleted books: " + pd.getDeletedBooks());
           System.out.println("Results (runPurgeAuthorsByAgeInBatchesProcedure()): " + bookstoreService.runPurgeAuthorsByAgeInBatchesProcedure());
        };
    }
}
