package com.bindr.dto;

import java.util.*;


// ValoracionDTO
class ValoracionDTO {
    private String id;
    private Date fechaCreacion;
    private String contenidoId;
    private int puntuacion;
    private String comentario;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getContenidoId() { return contenidoId; }
    public void setContenidoId(String contenidoId) { this.contenidoId = contenidoId; }
    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}