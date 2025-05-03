package com.bindr.dto;

import java.util.*;

// EstudianteDTO
public class EstudianteDTO {
    private String id;
    private Date fechaCreacion;
    private String nombre;
    private String correo;
    private List<String> intereses;
    private List<String> contenidosPublicadosIds;
    private List<String> valoracionesIds;
    private List<String> conexionesIds;
    private List<String> gruposEstudioIds;
    private List<String> mensajesIds;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public List<String> getIntereses() { return intereses; }
    public void setIntereses(List<String> intereses) { this.intereses = intereses; }
    public List<String> getContenidosPublicadosIds() { return contenidosPublicadosIds; }
    public void setContenidosPublicadosIds(List<String> contenidosPublicadosIds) { this.contenidosPublicadosIds = contenidosPublicadosIds; }
    public List<String> getValoracionesIds() { return valoracionesIds; }
    public void setValoracionesIds(List<String> valoracionesIds) { this.valoracionesIds = valoracionesIds; }
    public List<String> getConexionesIds() { return conexionesIds; }
    public void setConexionesIds(List<String> conexionesIds) { this.conexionesIds = conexionesIds; }
    public List<String> getGruposEstudioIds() { return gruposEstudioIds; }
    public void setGruposEstudioIds(List<String> gruposEstudioIds) { this.gruposEstudioIds = gruposEstudioIds; }
    public List<String> getMensajesIds() { return mensajesIds; }
    public void setMensajesIds(List<String> mensajesIds) { this.mensajesIds = mensajesIds; }
}
