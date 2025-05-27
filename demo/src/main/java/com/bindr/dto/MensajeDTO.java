package com.bindr.dto;

import java.time.LocalDateTime;

import com.bindr.modelos.Mensaje;

public record MensajeDTO(
    EstudianteDTO autor,
    String contenido,
    LocalDateTime fecha
) {
     public static MensajeDTO fromEntity(Mensaje mensaje, EstudianteDTO autorDTO) {
        if (mensaje == null) return null;

        return new MensajeDTO(
            autorDTO,
            mensaje.getContenido(),
            mensaje.getFecha()
        );
    }

    public Mensaje toEntity() {
        return Mensaje.builder()
            .autor(autor.toEntity())
            .contenido(contenido)
            .fecha(fecha)
            .build();
    }
}
