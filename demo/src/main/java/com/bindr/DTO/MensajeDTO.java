package com.bindr.dto;

import java.util.*;


// MensajeDTO
class MensajeDTO {
    private String id;
    private Date fechaCreacion;
    private String remitenteId;
    private String destinatarioId;
    private String contenido;
    private boolean leido;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getRemitenteId() { return remitenteId; }
    public void setRemitenteId(String remitenteId) { this.remitenteId = remitenteId; }
    public String getDestinatarioId() { return destinatarioId; }
    public void setDestinatarioId(String destinatarioId) { this.destinatarioId = destinatarioId; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }
}
