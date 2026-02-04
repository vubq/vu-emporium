package com.ecommerce.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = false)
@Slf4j
public class JpaJsonConverter implements AttributeConverter<Map<String, Map<String, String>>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Map<String, String>> meta) {
        if (meta == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            log.error("JSON writing error", ex);
            return null;
        }
    }

    @Override
    public Map<String, Map<String, String>> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<Map<String, Map<String, String>>>() {
            });
        } catch (IOException ex) {
            log.error("JSON reading error", ex);
            return new HashMap<>();
        }
    }
}
