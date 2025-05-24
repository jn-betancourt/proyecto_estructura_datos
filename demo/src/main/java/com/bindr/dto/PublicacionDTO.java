package com.bindr.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.bindr.modelos.MateriaEstudio;

public record PublicacionDTO(
    Long id,
    EstudianteDTO publicador,
    LocalDateTime fecha,
    String titulo,
    List<MateriaEstudio> materias
) {}
