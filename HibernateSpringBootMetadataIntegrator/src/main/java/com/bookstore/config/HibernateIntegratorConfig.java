package com.bookstore.config;

import java.util.List;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateIntegratorConfig {

    @Bean
    public HibernatePropertiesCustomizer integratorCustomizer(
            DatabaseTableMetadataExtractor extractor) {
        IntegratorProvider provider = () -> List.of(extractor);
        return properties -> properties.put("hibernate.integrator_provider", provider);
    }
}