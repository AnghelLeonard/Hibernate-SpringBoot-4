package com.citylots.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
public class JdbcConcurrentBatchService {

    private static final Logger logger = Logger.getLogger(JdbcConcurrentBatchService.class.getName());

    @Value("${jdbc.batch.size}")
    private int batchSize;

    private final JdbcBatcher jdbcBatcher;

    public JdbcConcurrentBatchService(JdbcBatcher jdbcBatcher) {
        this.jdbcBatcher = jdbcBatcher;
    }

    public void fileToDatabase(String fileName)
            throws IOException, InterruptedException, ExecutionException {

        logger.info("Reading file lines into a List<String> ...");

        // fetch 200000+ lines
        List<String> allLines = Files.readAllLines(Path.of(fileName));

        logger.info(() -> "Read a total of " + allLines.size()
                + " lines, inserting in batches of " + batchSize);

        logger.info("Start inserting ...");

        StopWatch watch = new StopWatch();
        watch.start();

        List<JdbcBatcher.BatchResult> results = jdbcBatcher.executeParallelBatch(allLines);

        watch.stop();

        logger.log(Level.INFO, "Stop inserting. \n Total time: {0} ms ({1} s)",
                new Object[]{watch.getTotalTimeMillis(), watch.getTotalTimeSeconds()});

        int totalSuccessRows = results.stream()
                .filter(JdbcBatcher.BatchResult::isSuccess)
                .mapToInt(JdbcBatcher.BatchResult::rowsAffected)
                .sum();

        long totalFailed = results.stream()
                .filter(r -> !r.isSuccess())
                .count();

        System.out.println("Batching is done! Executed " + results.size() + " batches. Saved rows: "
                + totalSuccessRows + " Fails: " + totalFailed);
    }
}
