package com.bookstore.forum.config;

import io.hypersistence.utils.hibernate.type.util.ObjectMapperSupplier;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

/**
 * Supplies a custom Jackson {@link ObjectMapper} for Hypersistence Utils'
 * {@code JsonType}. This one applies the {@code snake_case} property naming
 * strategy, so a camelCase Java field such as {@code flairLabel} is stored under
 * the JSON key {@code flair_label}.
 *
 * <p>It is wired in per persistence unit through the Hibernate property
 * {@code hypersistence.utils.jackson.object.mapper} (see
 * {@code application.properties}), which {@code JsonType} reads from the
 * {@code TypeBootstrapContext} configuration settings. This is the
 * JsonType-specific counterpart to Hibernate's global
 * {@code hibernate.type.json_format_mapper}, and it lets JsonType columns use a
 * different ObjectMapper from the rest of the application.</p>
 */
public class SnakeCaseObjectMapperSupplier implements ObjectMapperSupplier {

    @Override
    public ObjectMapper get() {
        return JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .findAndAddModules()
            .build();
    }
}
