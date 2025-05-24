package com.bindr.converters;

import java.util.List;

import com.bindr.modelos.MateriaEstudio;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public class MateriaEnumListConverter implements AttributeConverter<List<MateriaEstudio>, String> {
     private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<MateriaEstudio> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new IllegalStateException("Error serializando materias", e);
        }
    }

    @Override
    public List<MateriaEstudio> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Error deserializando materias", e);
        }
    }
}