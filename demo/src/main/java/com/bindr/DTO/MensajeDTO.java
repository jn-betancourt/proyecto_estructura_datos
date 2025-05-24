package com.bindr.dto;

import java.time.LocalDateTime;

public record MensajeDTO(
    Long id,
    Long autorId,
    String contenido,
    LocalDateTime fecha
) {}
