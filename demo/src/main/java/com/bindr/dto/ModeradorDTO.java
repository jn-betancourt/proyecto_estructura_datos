package com.bindr.dto;

import com.bindr.modelos.Moderador;

public record ModeradorDTO(Long id, String nombre, String correo) {
    
    // de entidad a DTO
    public static ModeradorDTO fromEntity(Moderador moderador) {
        return new ModeradorDTO(moderador.getId(), moderador.getNombre(), moderador.getCorreo());
    }

    // de DTO a entidad
    public Moderador toEntity() {
        return new Moderador.Builder()
                .id(id)
                .nombre(nombre)
                .correo(correo)
                .build();
    }
    
}
