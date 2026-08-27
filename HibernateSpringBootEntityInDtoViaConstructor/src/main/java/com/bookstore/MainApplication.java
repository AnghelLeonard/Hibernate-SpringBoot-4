package com.bookstore;

import com.bookstore.service.BookstoreService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.bookstore.dto.BookstoreDto;
import com.bookstore.dto.BookstoreRecord;

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

            List<BookstoreDto> authors1 = bookstoreService.fetchAuthorsDto();
            authors1.forEach(a -> System.out.println(a.getAuthor()
                    + ", Title: " + a.getTitle()));
            
            List<BookstoreRecord> authors2 = bookstoreService.fetchAuthorsRecord();
            authors2.forEach(a -> System.out.println(a.author()
                    + ", Title: " + a.title()));
        };
    }
}
