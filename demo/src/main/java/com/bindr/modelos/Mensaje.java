package com.bindr.modelos;

import java.util.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mensaje {
    private Long autorId;
    private String texto;
    private LocalDateTime fecha;
    private List<String> archivosAdjuntos;
    private Map<String, List<Long>> reacciones;

    public Mensaje() {
        this.archivosAdjuntos = new ArrayList<>();
        this.reacciones = new HashMap<>();
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<String> getArchivosAdjuntos() {
        return archivosAdjuntos;
    }

    public void setArchivosAdjuntos(List<String> archivosAdjuntos) {
        this.archivosAdjuntos = archivosAdjuntos;
    }

    public Map<String, List<Long>> getReacciones() {
        return reacciones;
    }

    public void setReacciones(Map<String, List<Long>> reacciones) {
        this.reacciones = reacciones;
    }

    public void agregarArchivo(String archivo) {
        archivosAdjuntos.add(archivo);
    }

    public void agregarReaccion(String tipo, Long usuarioId) {
        reacciones.putIfAbsent(tipo, new ArrayList<>());
        reacciones.get(tipo).add(usuarioId);
    }

    public boolean contieneReaccion(String tipo, Long usuarioId) {
        return reacciones.containsKey(tipo) && reacciones.get(tipo).contains(usuarioId);
    }

    public static class Builder {
        private Long autorId;
        private String texto;
        private LocalDateTime fecha;
        private List<String> archivosAdjuntos = new ArrayList<>();
        private Map<String, List<Long>> reacciones = new HashMap<>();

        public Builder autorId(Long autorId) {
            this.autorId = autorId;
            return this;
        }

        public Builder texto(String texto) {
            this.texto = texto;
            return this;
        }

        public Builder fecha(LocalDateTime fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder archivosAdjuntos(List<String> archivosAdjuntos) {
            this.archivosAdjuntos = archivosAdjuntos;
            return this;
        }

        public Builder reacciones(Map<String, List<Long>> reacciones) {
            this.reacciones = reacciones;
            return this;
        }

        public Mensaje build() {
            Mensaje mensaje = new Mensaje();
            mensaje.setAutorId(autorId);
            mensaje.setTexto(texto);
            mensaje.setFecha(fecha);
            mensaje.setArchivosAdjuntos(archivosAdjuntos);
            mensaje.setReacciones(reacciones);
            return mensaje;
        }
    }
}
