package com.bookstore.forum.config;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.format.FormatMapper;
import org.hibernate.type.format.jackson.Jackson3JsonFormatMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

/**
 * A Hibernate {@link FormatMapper} for the <strong>native</strong> JSON feature
 * ({@code @JdbcTypeCode(SqlTypes.JSON)}) that applies the {@code snake_case}
 * Jackson property naming strategy &mdash; the native counterpart of the
 * {@code JsonType}-side {@link SnakeCaseObjectMapperSupplier}.
 *
 * <p>It is selected through the Hibernate property
 * {@code hibernate.type.json_format_mapper} (see {@code application.properties}),
 * which is the native equivalent of
 * {@code hypersistence.utils.jackson.object.mapper}. Because it has a public
 * no-argument constructor, Hibernate can instantiate it straight from the
 * property value's class name.</p>
 *
 * <p>Hibernate 7.3 ships {@link Jackson3JsonFormatMapper} (the Jackson 3 mapper
 * used by this book's stack) with a constructor that accepts a custom
 * {@link JsonMapper}; that mapper is {@code final}, so this class simply wraps a
 * snake_case-configured instance and delegates to it.</p>
 */
public class SnakeCaseJsonFormatMapper implements FormatMapper {

    private final FormatMapper delegate;

    public SnakeCaseJsonFormatMapper() {
        JsonMapper jsonMapper = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .findAndAddModules()
            .build();
        this.delegate = new Jackson3JsonFormatMapper(jsonMapper);
    }

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
