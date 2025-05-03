package com.bindr.dto;

import java.util.*;

// ContenidoEducativoDTO
class ContenidoEducativoDTO {
    private String id;
    private Date fechaCreacion;
    private String titulo;
    private String autorId;
    private String tipo;
    private List<String> valoracionesIds;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutorId() { return autorId; }
    public void setAutorId(String autorId) { this.autorId = autorId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public List<String> getValoracionesIds() { return valoracionesIds; }
    public void setValoracionesIds(List<String> valoracionesIds) { this.valoracionesIds = valoracionesIds; }
}