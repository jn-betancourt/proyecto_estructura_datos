package com.bindr.modelos;

import java.util.*;


import java.time.LocalDateTime;

public class SolicitudAyuda {
    private Integer id;
    private Estudiante estudiante;
    private TipoSolicitud tipo;
    private Destinatario destinatario;
    private LocalDateTime fecha;
    private boolean resuelta;

    public SolicitudAyuda() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public TipoSolicitud getTipo() {
        return tipo;
    }

    public void setTipo(TipoSolicitud tipo) {
        this.tipo = tipo;
    }

    public Destinatario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Destinatario destinatario) {
        this.destinatario = destinatario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isResuelta() {
        return resuelta;
    }

    public void setResuelta(boolean resuelta) {
        this.resuelta = resuelta;
    }

    public void marcarComoResuelta() {
        this.resuelta = true;
    }

    public static class Builder {
        private Integer id;
        private Estudiante estudiante;
        private TipoSolicitud tipo;
        private Destinatario destinatario;
        private LocalDateTime fecha;
        private boolean resuelta;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder estudiante(Estudiante estudiante) {
            this.estudiante = estudiante;
            return this;
        }

        public Builder tipo(TipoSolicitud tipo) {
            this.tipo = tipo;
            return this;
        }

        public Builder destinatario(Destinatario destinatario) {
            this.destinatario = destinatario;
            return this;
        }

        public Builder fecha(LocalDateTime fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder resuelta(boolean resuelta) {
            this.resuelta = resuelta;
            return this;
        }

        public SolicitudAyuda build() {
            SolicitudAyuda solicitud = new SolicitudAyuda();
            solicitud.setId(id);
            solicitud.setEstudiante(estudiante);
            solicitud.setTipo(tipo);
            solicitud.setDestinatario(destinatario);
            solicitud.setFecha(fecha);
            solicitud.setResuelta(resuelta);
            return solicitud;
        }
    }
}
