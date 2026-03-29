package com.bookstore;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) throws RunnerException {

        var options = new OptionsBuilder()
                .include(BC.class.getSimpleName())
                .build();

        new Runner(options).run();
    }
}