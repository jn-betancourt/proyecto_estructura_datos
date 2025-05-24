package com.bindr.dto;

import java.time.LocalDateTime;

public record MensajeDTO(
    EstudianteDTO autor,
    String contenido,
    LocalDateTime fecha
) {}
