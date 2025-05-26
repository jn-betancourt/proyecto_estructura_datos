package com.bindr.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.bindr.modelos.MateriaEstudio;
import com.bindr.modelos.Publicacion;
import com.bindr.modelos.Valoracion;

public record PublicacionDTO(
        Long id,
        EstudianteDTO publicador,
        LocalDateTime fecha,
        String titulo,
        List<MateriaEstudio> materias,
        List<ValoracionDTO> valoraciones,
        String archivo // nueva propiedad
) {
    public static PublicacionDTO fromEntity(Publicacion publicacion) {
        if (publicacion == null) return null;
        List<ValoracionDTO> valoraciones = publicacion.getValoraciones().stream()
            .map(ValoracionDTO::fromEntity)
            .toList();
        return new PublicacionDTO(
                publicacion.getId(),
                EstudianteDTO.fromEntity(publicacion.getPublicador()),
                publicacion.getFecha(),
                publicacion.getTitulo(),
                publicacion.getMaterias(),
                valoraciones,
                publicacion.getArchivo()
        );
    }

    public Publicacion toEntity() {
        Publicacion pub = new Publicacion();
        // Convertir las valoraciones de DTOs a entidades
        List<Valoracion> valoraciones = this.valoraciones.stream()
                .map(ValoracionDTO::toEntity)
                .toList();
        pub.setId(id);
        pub.setTitulo(titulo);
        pub.setFecha(fecha);
        pub.setPublicador(publicador != null ? publicador.toEntity() : null);
        pub.setMaterias(materias);
        pub.setValoraciones(valoraciones);
        pub.setArchivo(archivo); // nueva propiedad
        return pub;
    }
}