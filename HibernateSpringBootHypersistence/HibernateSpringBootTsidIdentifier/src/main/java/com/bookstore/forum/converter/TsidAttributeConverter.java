package com.bookstore.forum.converter;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Persists a {@link TSID}-typed attribute as a {@code bigint} column and reads it
 * back as a {@link TSID}. {@code @Tsid} can generate values for {@code Long},
 * {@code String} and {@link TSID} fields; the {@link TSID} type keeps the full
 * value object in the domain model, and this converter maps it to the compact
 * 64-bit storage form (the same 8 bytes as the {@code Long} variant).
 */
@Converter
public class TsidAttributeConverter implements AttributeConverter<TSID, Long> {

    @Override
    public Long convertToDatabaseColumn(TSID tsid) {
        return tsid != null ? tsid.toLong() : null;
    }

    @Override
    public TSID convertToEntityAttribute(Long value) {
        return value != null ? TSID.from(value) : null;
    }
}
