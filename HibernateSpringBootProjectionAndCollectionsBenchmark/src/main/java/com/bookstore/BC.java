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
    public void bFetchAuthorsWithBooksQueryBuilderMechanism() {
        System.out.println("\n\nFetch authors with books via query builder mechanism");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchAuthorsWithBooksQueryBuilderMechanism();
    }

    @Benchmark
    public void bFetchAuthorsWithBooksViaQuery() {
        System.out.println("\n\nFetch authors with books via JPQL query");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchAuthorsWithBooksViaQuery();
    }

    @Benchmark
    public void bFetchAuthorsWithBooksViaJoinFetch() {
        System.out.println("\n\nFetch authors with books via JOIN FETCH");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchAuthorsWithBooksViaJoinFetch();
    }

    @Benchmark
    public void bFetchAuthorsWithBooksViaQuerySimpleDto() {
        System.out.println("\n\nFetch authors with books via query and simple DTO");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchAuthorsWithBooksViaQuerySimpleDto();
    }

    @Benchmark
    public void bFetchAuthorsWithBooksViaArrayOfObjects() {
        System.out.println("\n\nFetch authors with books via array of objects:");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchAuthorsWithBooksViaArrayOfObjects();
    }

    @Benchmark
    public void bFetchAuthorsWithBooksViaArrayOfObjectsAndTransformToDto() {
        System.out.println("\n\nFetch authors with books via array of objects and transform to DTO:");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchAuthorsWithBooksViaArrayOfObjectsAndTransformToDto();
    }

    @Benchmark
    public void bFetchAuthorsWithBooksViaJdbcTemplateToDto() {
        System.out.println("\n\nFetch authors with books via JdbcTemplate as DTO:");
        System.out.println("-----------------------------------------------------------------");
        bookstoreService.fetchAuthorsWithBooksViaJdbcTemplateToDto();
    }
}
