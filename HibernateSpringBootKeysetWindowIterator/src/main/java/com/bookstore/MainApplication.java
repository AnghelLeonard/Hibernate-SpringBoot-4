package com.bookstore;

import com.bookstore.entity.Transaction;
import com.bookstore.repository.TransactionRepository;
import com.bookstore.service.TransactionExportService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final TransactionExportService transactionExportService;
    private final TransactionRepository transactionRepository;
    private final Random random = new Random();

    public MainApplication(
            TransactionExportService transactionExportService, TransactionRepository transactionRepository) {
        this.transactionExportService = transactionExportService;
        this.transactionRepository = transactionRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            
            if (transactionRepository.count() == 0) {
                System.out.println("Starting database population with mock transactions...");
                generateMockData(1500);
                System.out.println("Database successfully populated!");
            }
            
            transactionExportService.exportHighValueTransactions();
        };
    }

    private void generateMockData(int totalRecords) {

        int batchSize = 100;
        List<Transaction> batch = new ArrayList<>();
        String[] statuses = {"COMPLETED", "PENDING", "FAILED"};

        for (int i = 1; i <= totalRecords; i++) {

            String status = statuses[random.nextInt(statuses.length)];

            Double amount = 10.0 + (random.nextDouble() * 5000.0);
            Long userId = (long) (1000 + random.nextInt(9000));

            Instant processedAt = Instant.now().minus(random.nextInt(30), ChronoUnit.DAYS);

            Transaction tx = new Transaction(status, amount, userId, processedAt);
            batch.add(tx);

            if (i % batchSize == 0 || i == totalRecords) {
                transactionRepository.saveAll(batch);
                batch.clear();
                System.out.printf("Saved batch... Total records inserted: %d/%d%n", i, totalRecords);
            }
        }
    }
}
