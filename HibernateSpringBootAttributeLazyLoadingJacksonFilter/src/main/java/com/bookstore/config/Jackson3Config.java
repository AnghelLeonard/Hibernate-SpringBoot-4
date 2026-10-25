package com.bookstore.config;

import com.bookstore.filter.LazyBasicAttributeFilter;
import tools.jackson.databind.ser.std.SimpleFilterProvider;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Jackson3Config {

    @Bean
    public JsonMapperBuilderCustomizer configureJackson3Filters() {
        
        return jsonMapperBuilder -> {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                    .addFilter("lazyAttributeFilter", new LazyBasicAttributeFilter());
                        
            jsonMapperBuilder.filterProvider(filterProvider);
        };
    }
}
