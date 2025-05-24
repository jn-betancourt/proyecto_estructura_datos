package com.bindr.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.bindr.modelos.MateriaEstudio;
import com.bindr.modelos.Publicacion;

public record PublicacionDTO(
        Long id,
        EstudianteDTO publicador,
        LocalDateTime fecha,
        String titulo,
        List<MateriaEstudio> materias,
        String archivo // nueva propiedad
) {
    public static PublicacionDTO fromEntity(Publicacion publicacion) {
        if (publicacion == null) return null;

        return new PublicacionDTO(
                publicacion.getId(),
                EstudianteDTO.fromEntity(publicacion.getPublicador()),
                publicacion.getFecha(),
                publicacion.getTitulo(),
                publicacion.getMaterias(),
                publicacion.getArchivo()
        );
    }

    public Publicacion toEntity() {
        Publicacion pub = new Publicacion();
        pub.setId(id);
        pub.setTitulo(titulo);
        pub.setFecha(fecha);
        pub.setPublicador(publicador != null ? publicador.toEntity() : null);
        pub.setMaterias(materias);
        pub.setArchivo(archivo); // nueva propiedad
        return pub;
    }
}