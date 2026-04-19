package com.bookstore;

import com.bookstore.service.BookstoreService;
import java.io.IOException;
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
        context.close();
    }

    @Benchmark
    public void insert10000Authors8VT() { // set the benchmark data from BatchExecutor
        System.out.println("\n\n Insert records:");
        System.out.println("--------------------");
        bookstoreService.batchAuthors();
    }
}
