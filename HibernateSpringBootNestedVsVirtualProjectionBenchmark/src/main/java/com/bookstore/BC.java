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
    public void bFetchBooksWithAuthorsQueryBuilderMechanism() {
        System.out.println("\n\nFetch books with authors via query builder mechanism");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchBooksWithAuthorsQueryBuilderMechanism();
    }

    @Benchmark
    public void bFetchBooksWithAuthorsViaQuery() {
        System.out.println("\n\nFetch books with authors via JPQL query");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchBooksWithAuthorsViaQuery();
    }

    @Benchmark
    public void bFetchBooksWithAuthorsViaQuerySimpleDto() {
        System.out.println("\n\nFetch books with authors via query and simple DTO");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchBooksWithAuthorsViaQuerySimpleDto();
    }

    @Benchmark
    public void bFetchBooksWithAuthorsViaArrayOfObjects() {
        System.out.println("\n\nFetch books with authors via array of objects");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchBooksWithAuthorsViaArrayOfObjects();
    }

    @Benchmark
    public void bFetchBooksWithAuthorsViaQueryVirtualDto() {
        System.out.println("\n\nFetch books with authors via query and virtual DTO");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchBooksWithAuthorsViaQueryVirtualDto();
    }
}
