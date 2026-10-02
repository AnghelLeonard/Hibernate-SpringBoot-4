package com.bookstore.service;

import com.bookstore.entity.Transaction;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.stereotype.Service;
import com.bookstore.repository.TransactionRepository;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.data.support.WindowIterator;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class TransactionExportService {

    private final TransactionRepository transactionRepository;

    public TransactionExportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Scheduled(cron = "0 0 2 * * *") // Runs every night at 2 AM    
    public void exportHighValueTransactions() {
        
        String csvFile = "exports/high_value_transactions_" + System.currentTimeMillis() + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            
            // Write CSV Header
            writer.write("TransactionID,Amount,UserID,ProcessedAt\n");

            // Initialize the WindowIterator using a Keyset cursor
            WindowIterator<Transaction> transactionIterator = WindowIterator.of(
                position -> transactionRepository.findFirst100ByStatusAndAmountGreaterThanOrderByProcessedAtDescIdDesc(
                        "COMPLETED", 1000.0, position
                )
            ).startingAt(ScrollPosition.keyset()); 

            // Transparently stream millions of rows while keeping memory footprint down to 100 objects
            while (transactionIterator.hasNext()) { 
                Transaction tx = transactionIterator.next(); 
                
                String line = String.format("%d,%.2f,%d,%s\n", 
                        tx.getId(), tx.getAmount(), tx.getUserId(), tx.getProcessedAt());
                writer.write(line);
            }
            
            System.out.println("Export completed successfully to: " + csvFile);

        } catch (IOException e) {
            System.err.println("Failed to write CSV file: " + e.getMessage());
        }
    }
}
