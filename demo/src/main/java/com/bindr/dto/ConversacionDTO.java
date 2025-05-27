package com.bindr.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.bindr.modelos.Conversacion;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.Mensaje;

public record ConversacionDTO(
    Long id,
    LocalDateTime fechaCreacion,
    List<EstudianteDTO> participantes,
    List<MensajeDTO> mensajes,
    boolean esGrupo
)
{
    public static ConversacionDTO fromEntity(Conversacion conversacion) {
        if (conversacion == null) return null;

        // Convertir participantes a DTO con tipo explícito
        List<EstudianteDTO> participantesDTO = conversacion.getParticipantes().stream()
            .map(EstudianteDTO::fromEntity)
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
                Estudiante autor = mensaje.getAutor();
                EstudianteDTO autorDTO = autor != null ? autorMap.get(autor.getId()) : null;
                mensajesDTO.add(MensajeDTO.fromEntity(mensaje, autorDTO));
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

    public Conversacion toEntity() {
        Conversacion conversacion = Conversacion.builder() 
        .id(this.id())
        .fechaCreacion(this.fechaCreacion())
        .esGrupo(this.esGrupo()).build();

        // Convertir participantes
        if (this.participantes() != null) {
            List<Estudiante> participantesEntity = this.participantes().stream()
                .map(EstudianteDTO::toEntity)
                .collect(Collectors.toList());
            conversacion.setParticipantes(participantesEntity);
        }

        // Convertir mensajes
        if (this.mensajes() != null) {
            List<Mensaje> mensajesEntity = this.mensajes().stream()
                .map(MensajeDTO::toEntity)
                .collect(Collectors.toList());
            conversacion.setMensajes(mensajesEntity);
        }

        return conversacion;
    }

}
