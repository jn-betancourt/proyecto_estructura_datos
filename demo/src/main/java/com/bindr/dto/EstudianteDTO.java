package com.bindr.dto;

import com.bindr.modelos.Estudiante;

public record EstudianteDTO(
    Long id,
    String nombre,
    String correo
) {

      public static EstudianteDTO fromEntity(Estudiante estudiante) {
        if (estudiante == null) return null;

        return new EstudianteDTO(
            estudiante.getId(),
            estudiante.getNombre(),
            estudiante.getCorreo()
        );
    }

    public Estudiante toEntity() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(id);
        estudiante.setNombre(nombre);
        estudiante.setCorreo(correo);
        // contraseña no se mapea aquí por razones de seguridad
        return estudiante;
    }

}