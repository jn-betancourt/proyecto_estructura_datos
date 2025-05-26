package com.bindr.dto;

import java.time.LocalDate;

import com.bindr.modelos.Valoracion;

public record ValoracionDTO(
        Long id,
        String autor,
        boolean like,
        LocalDate fecha
) {
    public static ValoracionDTO fromEntity(Valoracion valoracion) {
        if (valoracion == null) return null;

        // En la entidad, autor es un String (correo)
        return new ValoracionDTO(
                valoracion.getId(),
                valoracion.getAutor(),
                valoracion.isLike(),
                valoracion.getFecha()
        );
    }

    public Valoracion toEntity() {
        return Valoracion.builder()
                .id(id)
                .autor(autor) // usa el campo autor (correo)
                .like(like)
                .fecha(fecha)
                .build();
    }
    
}
