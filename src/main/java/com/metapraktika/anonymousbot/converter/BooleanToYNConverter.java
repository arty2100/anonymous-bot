package com.metapraktika.anonymousbot.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter()
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean value) {
        if (value == null) return "N";
        return value ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "Y".equalsIgnoreCase(value);
    }
}