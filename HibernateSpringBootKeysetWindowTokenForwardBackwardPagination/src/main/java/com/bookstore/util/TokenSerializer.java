package com.bookstore.util;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public final class TokenSerializer {

    // Suppress default constructor for utility class
    private TokenSerializer() {}
   
    public static String serialize(Map<String, ?> keys) {
        if (keys == null || keys.isEmpty()) {
            return null;
        }

        // Allocate exact or close-to-exact buffer capacity to prevent array resizing
        StringBuilder sb = new StringBuilder(48);
        sb.append("id=").append(keys.get("id"))
          .append(";createdAt=").append(keys.get("createdAt"));

        return sb.toString();
    }

    /**
     * Parses the token string using a single-pass index scanner.
     * Zero regex overhead, minimal object allocations.
     */
    public static Map<String, Object> deserialize(String token) {
        if (token == null || token.isEmpty()) {
            return Map.of();
        }

        // Pre-size to 2 entries with a 1.0 load factor to completely prevent internal map resizing
        Map<String, Object> keys = new HashMap<>(2, 1.0f);

        int len = token.length();
        int start = 0;

        while (start < len) {
            // Find key-value delimiter
            int eqIdx = token.indexOf('=', start);
            if (eqIdx == -1) break;

            // Find pair delimiter
            int semiIdx = token.indexOf(';', eqIdx);
            if (semiIdx == -1) {
                semiIdx = len; // Last pair in string
            }

            // Extract substrings using direct index boundaries
            String key = token.substring(start, eqIdx);
            String val = token.substring(eqIdx + 1, semiIdx);

            // Fast matching logic
            if ("id".equals(key)) {
                keys.put("id", Long.parseLong(val));
            } else if ("createdAt".equals(key)) {
                keys.put("createdAt", Instant.parse(val));
            }

            // Move pointer past the semicolon
            start = semiIdx + 1;
        }

        return keys;
    }
}