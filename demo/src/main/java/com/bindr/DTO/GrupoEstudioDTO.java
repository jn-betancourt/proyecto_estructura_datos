package com.bindr.dto;

import java.util.*;


// GrupoEstudioDTO
class GrupoEstudioDTO {
    private String id;
    private Date fechaCreacion;
    private String tema;
    private List<String> estudiantesIds;
    private List<String> contenidosRelacionadosIds;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    public List<String> getEstudiantesIds() { return estudiantesIds; }
    public void setEstudiantesIds(List<String> estudiantesIds) { this.estudiantesIds = estudiantesIds; }
    public List<String> getContenidosRelacionadosIds() { return contenidosRelacionadosIds; }
    public void setContenidosRelacionadosIds(List<String> contenidosRelacionadosIds) { this.contenidosRelacionadosIds = contenidosRelacionadosIds; }
}

