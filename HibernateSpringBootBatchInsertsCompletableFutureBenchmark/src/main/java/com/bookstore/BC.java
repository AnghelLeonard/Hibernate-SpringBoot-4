package com.bookstore;

import com.bookstore.impl.BatchExecutor;
import com.bookstore.service.BookstoreService;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class BC {

    private static final Logger logger = Logger.getLogger(BC.class.getName());

    private BookstoreService bookstoreService;
    private ConfigurableApplicationContext context;

    @Setup(Level.Trial)
    public void setUp() throws IOException {

        if (context == null) {
            context = new SpringApplicationBuilder(MainApplication.class)
                    .properties("server.port=0")
                    .run();
        }

        bookstoreService = context.getBean(BookstoreService.class);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        shutdownExecutor(BatchExecutor.executor);
        context.close();
    }

    private static boolean shutdownExecutor(ExecutorService es) {
        es.shutdown();
        try {
            if (!es.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                es.shutdownNow();

                return es.awaitTermination(10000, TimeUnit.MILLISECONDS);
            }

            return true;
        } catch (InterruptedException e) {
            es.shutdownNow();
            Thread.currentThread().interrupt();
            logger.severe(() -> "Shutdown Exception: " + e);
        }
        return false;
    }

    @Benchmark
    public void insertEntities() {
        System.out.println("\n\n Insert entities:");
        System.out.println("---------------------");
        bookstoreService.batchAuthors();
    }
}
