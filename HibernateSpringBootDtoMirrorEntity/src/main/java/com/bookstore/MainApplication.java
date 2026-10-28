package com.bookstore;
 
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

            System.out.println("\n\n Calling fetchViafindAll():");
            bookstoreService.fetchViafindAll();
            
            System.out.println("\n\n Calling fetchViafindBy():");
            bookstoreService.fetchViafindBy();
            
            System.out.println("\n\n Calling fetchAsDto():");
            bookstoreService.fetchAsDto();
            
            System.out.println("\n\n Calling fetchAsRecordDto():");
            bookstoreService.fetchAsRecordDto();
            
            System.out.println("\n\n Calling fetchAsDtoColumns():");
            bookstoreService.fetchAsDtoColumns();
            
            System.out.println("\n\n Calling fetchAsDtoNative():");
            bookstoreService.fetchAsDtoNative();
        };
    }
}
