package com.bookstore;

import com.bookstore.service.BookstoreService;
import java.util.concurrent.TimeUnit;
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

    private BookstoreService bookstoreService;
    private ConfigurableApplicationContext context;

    @Setup(Level.Trial)
    public void setUp() {

        if (context == null) {
            context = new SpringApplicationBuilder(MainApplication.class)
                    .properties("server.port=0")
                    .run();
        }

        bookstoreService = context.getBean(BookstoreService.class);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        context.close();
    }

    @Benchmark
    public void tBatch5AuthorsAnd5Books() {
        System.out.println("\n\nBatch 5 authors with 5 books");
        System.out.println("--------------------------------");
        bookstoreService.batch5AuthorsAnd5Books();
    }
    
    @Benchmark
    public void tbatch25AuthorsAnd5Books() {
        System.out.println("\n\nBatch 25 authors with 5 books");
        System.out.println("---------------------------------");
        bookstoreService.batch25AuthorsAnd5Books();
    }
    
    @Benchmark
    public void tBatch50AuthorsAnd5Books() {
        System.out.println("\n\nBatch 50 authors with 5 books");
        System.out.println("---------------------------------");
        bookstoreService.batch50AuthorsAnd5Books();
    }
    
    @Benchmark
    public void tBatch100AuthorsAnd5Books() {
        System.out.println("\n\nBatch 100 authors with 5 books");
        System.out.println("----------------------------------");
        bookstoreService.batch100AuthorsAnd5Books();
    }
    
    @Benchmark
    public void tBatch500AuthorsAnd5Books() {
        System.out.println("\n\nBatch 500 authors with 5 books");
        System.out.println("----------------------------------");
        bookstoreService.batch500AuthorsAnd5Books();
    }
}