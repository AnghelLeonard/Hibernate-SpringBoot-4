package com.bookstore.forum.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.hypersistence.utils.hibernate.type.util.ObjectMapperSupplier;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

/**
 * Supplies a custom Jackson {@link ObjectMapper} to Hypersistence Utils'
 * {@code JsonType}, wired in through the
 * {@code hypersistence.utils.jackson.object.mapper} property (see
 * {@code application.properties}). It applies the two customizations that
 * actually differ from the Jackson 3 defaults: the {@code snake_case} property
 * naming strategy, and {@code NON_NULL} inclusion, so {@code null} fields are
 * left out of the stored JSON. On Jackson 3, java.time values already serialize
 * as ISO-8601 strings and unknown JSON keys are ignored on read, so neither
 * needs configuring here. This is the {@code JsonType}-specific counterpart to
 * Hibernate's native {@code hibernate.type.json_format_mapper}.
 */
public class CustomObjectMapperSupplier implements ObjectMapperSupplier {

    // tag::mapper[]
    @Override
    public ObjectMapper get() {
        return JsonMapper.builder()
            .findAndAddModules()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .changeDefaultPropertyInclusion(
                inclusion -> inclusion.withValueInclusion(JsonInclude.Include.NON_NULL))
            .build();
    }
    // end::mapper[]
}
