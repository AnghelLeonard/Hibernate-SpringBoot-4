package com.bookstore;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.AuthorRecord;
import java.util.List;
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

            List<AuthorDto> authorsDto = bookstoreService.fetchAuthorsDto();

            System.out.println("Number of authors:" + authorsDto.size());

            for (AuthorDto author : authorsDto) {
                System.out.println("Author name: " + author.getName()
                        + " | Age: " + author.getAge());
            }
            
            List<AuthorRecord> authorsRecord = bookstoreService.fetchAuthorsRecord();

            System.out.println("Number of authors:" + authorsRecord.size());

            for (AuthorRecord author : authorsRecord) {
                System.out.println("Author name: " + author.name()
                        + " | Age: " + author.age());
            }
        };
    }
}
