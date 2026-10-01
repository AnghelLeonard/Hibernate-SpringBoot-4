package com.bookstore.util;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
public class KeysetTokenEncoder {

    private final ObjectMapper objectMapper;

    public KeysetTokenEncoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Base64 -> ScrollPosition
    public ScrollPosition decode(String token) {
        
        if (token == null || token.isBlank()) {
            return ScrollPosition.keyset(); // first page
        }
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(token);
            Map<String, Object> keys = objectMapper.readValue(decodedBytes, new TypeReference<>() {});
            
            // convert date-string to Instant
            if (keys.containsKey("createdAt") && keys.get("createdAt") instanceof String dateStr) {
                keys.put("createdAt", Instant.parse(dateStr));
            }
            
            return ScrollPosition.forward(keys);
        } catch (JacksonException e) {
            // invalid token, go to first page
            return ScrollPosition.keyset();
        }
    }

    // last position in Window -> Base64
    public String encode(ScrollPosition position) {
        
        if (position instanceof KeysetScrollPosition keysetPosition) {
            try {
                Map<String, Object> keys = keysetPosition.getKeys();
                byte[] jsonBytes = objectMapper.writeValueAsBytes(keys);
                return Base64.getUrlEncoder().encodeToString(jsonBytes);
            } catch (JacksonException e) {
                throw new RuntimeException("Cannot serialize the token", e);
            }
        }
        return null;
    }
}
