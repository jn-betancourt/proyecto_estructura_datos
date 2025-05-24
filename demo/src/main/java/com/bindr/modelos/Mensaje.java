package com.bindr.modelos;

import java.time.LocalDateTime;

public class Mensaje {

    private Long id;
    private Long autorId;
    private String contenido;
    private LocalDateTime fecha;

    // Constructor privado para forzar uso de Builder
    private Mensaje(Long id, Long autorId, String contenido, LocalDateTime fecha) {
        this.id = id;
        this.autorId = autorId;
        this.contenido = contenido;
        this.fecha = fecha;
    }

    // Constructor vacío privado para Jackson
    private Mensaje() {
    }

    // Método estático para obtener el Builder
    public static Builder builder() {
        return new Builder();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    // Builder
    public static class Builder {
        private Long id;
        private Long autorId;
        private String contenido;
        private LocalDateTime fecha;

        private Builder() {}

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder autorId(Long autorId) {
            this.autorId = autorId;
            return this;
        }

        public Builder contenido(String contenido) {
            this.contenido = contenido;
            return this;
        }

        public Builder fecha(LocalDateTime fecha) {
            this.fecha = fecha;
            return this;
        }

        public Mensaje build() {
            return new Mensaje(id, autorId, contenido, fecha);
        }
    }
}
