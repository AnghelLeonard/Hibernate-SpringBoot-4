package com.bookstore;

import com.bookstore.service.JpaMetamodelService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final JpaMetamodelService jpaMetamodelService;

    public MainApplication(JpaMetamodelService jpaMetamodelService) {
        this.jpaMetamodelService = jpaMetamodelService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            jpaMetamodelService.printJpaMetamodel();
        };
    }
}
