package com.bookstore;

import com.bookstore.impl.BatchRepositoryImpl;
import static java.util.GregorianCalendar.BC;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BatchRepositoryImpl.class)
public class MainApplication {

    public static void main(String[] args) throws RunnerException {

        var options = new OptionsBuilder()
                .include(BC.class.getSimpleName())
                .build();

        new Runner(options).run();
    }
}
