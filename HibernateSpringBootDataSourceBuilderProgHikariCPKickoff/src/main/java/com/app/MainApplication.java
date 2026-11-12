package com.app;

import com.app.service.HikariBenchmarkService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {
   
    private final HikariBenchmarkService hikariBenchmarkService;

    public MainApplication(HikariBenchmarkService hikariBenchmarkService) {
        this.hikariBenchmarkService = hikariBenchmarkService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Start test ...");
            
            hikariBenchmarkService.runSpikeTest(50, 100);

            System.out.println("End test ...");
        };
    }
}
