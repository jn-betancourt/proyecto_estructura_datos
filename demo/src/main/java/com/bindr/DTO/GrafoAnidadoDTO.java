package com.bindr.dto;

import java.util.*;


// GrafoAfinidadDTO
class GrafoAfinidadDTO {
    private Map<String, List<String>> conexiones;

    // Getters y Setters
    public Map<String, List<String>> getConexiones() { return conexiones; }
    public void setConexiones(Map<String, List<String>> conexiones) { this.conexiones = conexiones; }
}