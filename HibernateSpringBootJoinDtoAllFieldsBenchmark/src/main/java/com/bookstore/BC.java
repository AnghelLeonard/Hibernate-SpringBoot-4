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
    public void bFetchAuthorAsReadOnlyEntities() {
        System.out.println("\n\n Fetch authors read-only entities:");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsReadOnlyEntities();
    }

    @Benchmark
    public void bFetchAuthorAsArrayOfObject() {
        System.out.println("\n\n Fetch authors as array of objects");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsArrayOfObject();
    }

    @Benchmark
    public void bFetchAuthorAsArrayOfObjectColumns() {
        System.out.println("\n\n Fetch authors as array of objects by specifying columns");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsArrayOfObjectColumns();
    }

    @Benchmark
    public void bFetchAuthorAsArrayOfObjectNative() {
        System.out.println("\n\n Fetch authors as array of objects via native query");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsArrayOfObjectNative();
    }

    @Benchmark
    public void bFetchAuthorAsArrayOfObjectQueryBuilderMechanism() {
        System.out.println("\n\n Fetch authors as array of objects via query builder mechanism");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsArrayOfObjectQueryBuilderMechanism();
    }

    @Benchmark
    public void bFetchAuthorAsDtoClass() {
        System.out.println("\n\n Fetch authors as Spring projection (DTO):");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsDtoClass();
    }

    @Benchmark
    public void bFetchAuthorAsDtoClassColumns() {
        System.out.println("\n\n Fetch authors as Spring projection (DTO) by specifying columns:");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsDtoClassColumns();
    }

    @Benchmark
    public void bFetchAuthorAsDtoClassNative() {
        System.out.println("\n\n Fetch authors as Spring projection (DTO) and native query:");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsDtoClassNative();
    }

    @Benchmark
    public void bFetchAuthorAsDtoClassQueryBuilderMechanism() {
        System.out.println("\n\n Fetch authors as Spring projection (DTO) via query builder mechanism:");
        System.out.println("-----------------------------------------------------------------------------");
        bookstoreService.fetchAuthorAsDtoClassQueryBuilderMechanism();
    }
}
