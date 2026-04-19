package com.citylots;

import com.citylots.forkjoin.ForkJoinService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    // 'citylots.json' is available in the root of the app as a ZIP archive
    // before running the application, unzip this archive
    private static final String FILE_NAME = "citylots.json";
    private ForkJoinService forkJoinService;
    private ConfigurableApplicationContext context;
    private List<String> lines = new ArrayList<>();

    @Setup(Level.Trial)
    public void setUp() throws IOException {

        if (context == null) {
            context = new SpringApplicationBuilder(MainApplication.class)
                    .properties("server.port=0")
                    .run();
        }

        forkJoinService = context.getBean(ForkJoinService.class);

        if (lines.isEmpty()) {
            lines = forkJoinService.fileToDatabase(FILE_NAME);
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        context.close();
    }

    @Benchmark
    public void insertRecords() { // set the number of threads and records from ForkJoinService
        System.out.println("\n\n Insert records:");
        System.out.println("--------------------");
        forkJoinService.forkjoin(lines);
    }    
}
