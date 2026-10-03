package com.bookstore.utils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class Cursor {

    private static final String DELIMITER = "|";

    public record DecodedCursor(Instant createdAt, Long id) {

    }

    // Convert entity values into an opaque Base64 string
    public static String encode(Instant createdAt, Long id) {
        
        if (createdAt == null || id == null) {
            return null;
        }
        
        String rawCursor = createdAt.toString() + DELIMITER + id;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
                rawCursor.getBytes(StandardCharsets.UTF_8));
    }

    // Convert an opaque Base64 string back into entity values
    public static DecodedCursor decode(String token) {
        
        if (token == null || token.isBlank()) {
            return new DecodedCursor(null, null);
        }
        
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(token);
            String rawCursor = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] parts = rawCursor.split("\\" + DELIMITER);

            Instant createdAt = Instant.parse(parts[0]);
            Long id = Long.valueOf(parts[1]);
            return new DecodedCursor(createdAt, id);
            
        } catch (NumberFormatException e) {
            // Fallback to the first page if the token is malformed or invalid
            return new DecodedCursor(null, null);
        }
    }
}
