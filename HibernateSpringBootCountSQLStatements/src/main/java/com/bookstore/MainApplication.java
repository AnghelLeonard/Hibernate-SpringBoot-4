package com.bookstore;

import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.assertSelectCount;
import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.assertUpdateCount;
import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.assertDeleteCount;
import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.assertInsertCount;
import static io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator.reset;
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

            reset();
            bookstoreService.updateAuthorWithoutTransactional();
            // at this point there is no transaction running
            // there are 3 statements         
            assertSelectCount(2);
            assertUpdateCount(1);
            assertInsertCount(0);
            assertDeleteCount(0);

            reset();
            bookstoreService.updateAuthorWithTransactional();
            // allow the transaction to commit
            // there are 2 statements instead of 3
            assertSelectCount(1);
            assertUpdateCount(1);
            assertInsertCount(0);            
            assertDeleteCount(0);           
        };
    }
}
