package com.bindr.dto;

import java.time.LocalDateTime;

import com.bindr.modelos.NivelUrgencia;
import com.bindr.modelos.SolicitudAyuda;

public record SolicitudAyudaDTO(
    Integer id,
    EstudianteDTO remitente,
    EstudianteDTO destinatario,
    String materia,
    NivelUrgencia urgencia,
    LocalDateTime fecha,
    String estado,
    String mensaje
) {
    // Conversión de entidad a DTO
    public static SolicitudAyudaDTO fromEntity(SolicitudAyuda entidad) {
        if (entidad == null) return null;
        return new SolicitudAyudaDTO(
            entidad.getId(),
            EstudianteDTO.fromEntity(entidad.getRemitente()),
            entidad.getDestinatario() != null ? EstudianteDTO.fromEntity(entidad.getDestinatario()) : null,
            entidad.getMateria(),
            entidad.getUrgencia(),
            entidad.getFecha(),
            entidad.getEstado(),
            entidad.getMensaje()
        );
    }

    // Conversión de DTO a entidad (usando el builder)
    public SolicitudAyuda toEntity() {
        return new SolicitudAyuda.Builder()
            .id(id)
            .remitente(remitente != null ? remitente.toEntity() : null)
            .destinatario(destinatario != null ? destinatario.toEntity() : null)
            .materia(materia)
            .urgencia(urgencia)
            .fecha(fecha)
            .estado(estado)
            .mensaje(mensaje)
            .build();
    }
}
