package com.bindr.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ConversacionDTO(
    Long id,
    LocalDateTime fechaCreacion,
    List<EstudianteDTO> participantes,
    List<MensajeDTO> mensajes,
    boolean esGrupo
) {}
