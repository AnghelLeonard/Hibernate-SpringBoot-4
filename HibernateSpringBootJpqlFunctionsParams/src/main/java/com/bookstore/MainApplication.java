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
            System.out.println("callConcat() result: " + bookstoreService.callConcat());
            System.out.println("callCosRadians() result: " + bookstoreService.callCosRadians());
            System.out.println("callField() result: " + bookstoreService.callField());
            System.out.println("callApplyDiscount() result: " + bookstoreService.callApplyDiscount());
            System.out.println("callApplyDiscountInWhere() result: " + bookstoreService.callApplyDiscountInWhere());
            
            System.out.println("titleAndPriceEm() result: " + bookstoreService.titleAndPriceEm());            
            System.out.println("titleAndPriceGt25Em() result: " + bookstoreService.titleAndPriceGt25Em());            
            System.out.println("titleAndPriceCb() result: " + bookstoreService.titleAndPriceCb());
            System.out.println("titleAndPriceGt25Cb() result: " + bookstoreService.titleAndPriceGt25Cb());
        };
    }

}
