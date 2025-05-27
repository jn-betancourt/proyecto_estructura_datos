package com.bindr.dto;

import java.util.List;

import com.bindr.modelos.GrupoEstudio;
import com.bindr.modelos.MateriaEstudio;

public record GrupoEstudioDTO(
    Long id,
    String nombre,
    List<MateriaEstudio> materia,
    List<EstudianteDTO> estudiantes,
    ConversacionDTO conversacion
) {

    public static GrupoEstudioDTO fromEntity(GrupoEstudio grupo) {
        if (grupo == null) return null;

        List<EstudianteDTO> estudiantesDTO = grupo.getEstudiantes() != null
            ? grupo.getEstudiantes().stream()
                .map(EstudianteDTO::fromEntity)
                .toList()
            : List.of();

        return new GrupoEstudioDTO(
            grupo.getId(),
            grupo.getNombre(),
            grupo.getMateria(),
            estudiantesDTO,
            ConversacionDTO.fromEntity(grupo.getConversacion())
        );
    }

    public GrupoEstudio toEntity() {
        GrupoEstudio grupo = new GrupoEstudio();
        grupo.setId(id);
        grupo.setNombre(nombre);
        grupo.setMateria(materia);
        grupo.setEstudiantes(estudiantes != null
            ? estudiantes.stream().map(EstudianteDTO::toEntity).toList()
            : List.of());
        grupo.setConversacion(conversacion != null ? conversacion.toEntity() : null);
        return grupo;
    }
}
