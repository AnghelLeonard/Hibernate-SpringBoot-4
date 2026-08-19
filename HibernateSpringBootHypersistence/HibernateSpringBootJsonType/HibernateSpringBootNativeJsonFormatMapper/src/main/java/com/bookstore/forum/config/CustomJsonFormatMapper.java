package com.bookstore.forum.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.format.FormatMapper;
import org.hibernate.type.format.jackson.Jackson3JsonFormatMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

/**
 * A Hibernate {@link FormatMapper} for the <strong>native</strong> JSON feature
 * ({@code @JdbcTypeCode(SqlTypes.JSON)}), selected through
 * {@code hibernate.type.json_format_mapper} (see {@code application.properties}).
 * It builds the very same custom Jackson mapper as the {@code JsonType}-side
 * {@code CustomObjectMapperSupplier} &mdash; {@code snake_case} naming plus
 * {@code NON_NULL} inclusion &mdash; and delegates to Hibernate's
 * {@link Jackson3JsonFormatMapper}, whose {@code JsonMapper}-accepting
 * constructor is how a custom mapper is plugged into the native feature. Because
 * this class has a public no-argument constructor, Hibernate can instantiate it
 * straight from the property value's class name.
 */
public class CustomJsonFormatMapper implements FormatMapper {

    private final FormatMapper delegate;

    // tag::mapper[]
    public CustomJsonFormatMapper() {
        JsonMapper jsonMapper = JsonMapper.builder()
            .findAndAddModules()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .changeDefaultPropertyInclusion(
                inclusion -> inclusion.withValueInclusion(JsonInclude.Include.NON_NULL))
            .build();
        this.delegate = new Jackson3JsonFormatMapper(jsonMapper);
    }
    // end::mapper[]

    @Override
    public <T> T fromString(CharSequence charSequence, JavaType<T> javaType, WrapperOptions options) {
        return delegate.fromString(charSequence, javaType, options);
    }

    @Override
    public <T> String toString(T value, JavaType<T> javaType, WrapperOptions options) {
        return delegate.toString(value, javaType, options);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return delegate.supportsSourceType(sourceType);
    }

    @Override
    public boolean supportsTargetType(Class<?> targetType) {
        return delegate.supportsTargetType(targetType);
    }

    @Override
    public <T> void writeToTarget(T value, JavaType<T> javaType, Object target, WrapperOptions options)
            throws IOException {
        delegate.writeToTarget(value, javaType, target, options);
    }

    @Override
    public <T> T readFromSource(JavaType<T> javaType, Object source, WrapperOptions options)
            throws IOException {
        return delegate.readFromSource(javaType, source, options);
    }
}
