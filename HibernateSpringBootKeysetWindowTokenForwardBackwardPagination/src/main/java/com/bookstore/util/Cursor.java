package com.bookstore.util;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public final class Cursor {

    // suppress default constructor for utility class
    private Cursor() {}
   
    public static String encode(Map<String, ?> keys) {
        if (keys == null || keys.isEmpty()) {
            return null;
        }

        // allocate exact or close-to-exact buffer capacity to prevent array resizing
        StringBuilder sb = new StringBuilder(48);
        sb.append("id=").append(keys.get("id"))
          .append(";createdAt=").append(keys.get("createdAt"));

        return sb.toString();
    }
   
    public static Map<String, Object> decode(String token) {
        if (token == null || token.isEmpty()) {
            return Map.of();
        }

        // pre-size to 2 entries with a 1.0 load factor to completely prevent internal map resizing
        Map<String, Object> keys = new HashMap<>(2, 1.0f);

        int len = token.length();
        int start = 0;

        while (start < len) {
            // find key-value delimiter
            int eqIdx = token.indexOf('=', start);
            if (eqIdx == -1) break;

            // find pair delimiter
            int semiIdx = token.indexOf(';', eqIdx);
            if (semiIdx == -1) {
                semiIdx = len; // last pair in string
            }

            // extract substrings using direct index boundaries
            String key = token.substring(start, eqIdx);
            String val = token.substring(eqIdx + 1, semiIdx);

            // fast matching logic
            if ("id".equals(key)) {
                keys.put("id", Long.valueOf(val));
            } else if ("createdAt".equals(key)) {
                keys.put("createdAt", Instant.parse(val));
            }

            // move pointer past the semicolon
            start = semiIdx + 1;
        }

        return keys;
    }
}