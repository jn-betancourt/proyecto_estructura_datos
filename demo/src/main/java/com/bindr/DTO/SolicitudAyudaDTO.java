package com.bindr.dto;

import java.util.*;


// SolicitudAyudaDTO
class SolicitudAyudaDTO {
    private String id;
    private Date fechaCreacion;
    private String estudianteId;
    private String tema;
    private int urgencia;
    private String estado;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    public int getUrgencia() { return urgencia; }
    public void setUrgencia(int urgencia) { this.urgencia = urgencia; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
