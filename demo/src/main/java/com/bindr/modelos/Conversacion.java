package com.bindr.modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Conversacion {
    private Integer id;
    private String nombre;
    private List<Estudiante> participantes;
    private List<Mensaje> mensajes;
    private LocalDateTime fechaCreacion;
    private boolean esGrupo;

    public Conversacion() {
        this.participantes = new ArrayList<>();
        this.mensajes = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Estudiante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Estudiante> participantes) {
        this.participantes = participantes;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isEsGrupo() {
        return esGrupo;
    }

    public void setEsGrupo(boolean esGrupo) {
        this.esGrupo = esGrupo;
    }

    public void agregarParticipante(Estudiante estudiante) {
        if (!participantes.contains(estudiante)) {
            participantes.add(estudiante);
        }
    }

    public void removerParticipante(Estudiante estudiante) {
        participantes.remove(estudiante);
    }

    public void agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
    }

    public String obtenerNombreParaEstudiante(Estudiante estudiante) {
        if (!esGrupo && participantes.size() == 2) {
            for (Estudiante e : participantes) {
                if (!e.equals(estudiante)) {
                    return e.getNombre();
                }
            }
        }
        return nombre;
    }

    public static class Builder {
        private Integer id;
        private String nombre;
        private List<Estudiante> participantes = new ArrayList<>();
        private List<Mensaje> mensajes = new ArrayList<>();
        private LocalDateTime fechaCreacion;
        private boolean esGrupo;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder participantes(List<Estudiante> participantes) {
            this.participantes = participantes;
            return this;
        }

        public Builder mensajes(List<Mensaje> mensajes) {
            this.mensajes = mensajes;
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder esGrupo(boolean esGrupo) {
            this.esGrupo = esGrupo;
            return this;
        }

        public Conversacion build() {
            Conversacion conversacion = new Conversacion();
            conversacion.setId(id);
            conversacion.setNombre(nombre);
            conversacion.setParticipantes(participantes);
            conversacion.setMensajes(mensajes);
            conversacion.setFechaCreacion(fechaCreacion);
            conversacion.setEsGrupo(esGrupo);
            return conversacion;
        }
    }
}
