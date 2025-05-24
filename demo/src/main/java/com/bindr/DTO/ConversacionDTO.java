package com.bindr.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.bindr.modelos.Conversacion;
import com.bindr.modelos.Mensaje;

public record ConversacionDTO(
    Long id,
    LocalDateTime fechaCreacion,
    List<EstudianteDTO> participantes,
    List<MensajeDTO> mensajes,
    boolean esGrupo
) {
   public static ConversacionDTO fromEntity(Conversacion conversacion) {
    if (conversacion == null) return null;

    // Convertir participantes a DTO con tipo explícito
    List<EstudianteDTO> participantesDTO = conversacion.getParticipantes().stream()
        .map(est -> EstudianteDTO.fromEntity(est))
        .collect(Collectors.toList());

    // Crear un mapa auxiliar de ID -> EstudianteDTO
    Map<Long, EstudianteDTO> autorMap = new HashMap<>();
    for (EstudianteDTO dto : participantesDTO) {
        autorMap.put(dto.id(), dto);
    }

    // Convertir mensajes a DTO
    List<MensajeDTO> mensajesDTO = new ArrayList<>();
    if (conversacion.getMensajes() != null) {
        for (Mensaje mensaje : conversacion.getMensajes()) {
            EstudianteDTO autor = autorMap.get(mensaje.getAutorId());
            mensajesDTO.add(MensajeDTO.fromEntity(mensaje, autor));
        }
    }

    return new ConversacionDTO(
        conversacion.getId(),
        conversacion.getFechaCreacion(),
        participantesDTO,
        mensajesDTO,
        conversacion.isEsGrupo()
    );
}
}
