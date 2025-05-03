package com.bindr.modelos;

import java.util.*;


// Clase SolicitudAyuda
class SolicitudAyuda implements Comparable<SolicitudAyuda> {
    private String id; // Nuevo campo

    private Estudiante estudiante;
    private String tema;
    private int urgencia;
    private Date fecha;
    private String estado;

    public SolicitudAyuda(String id, Estudiante estudiante, String tema, int urgencia, Date fecha) {
        this.estudiante = estudiante;
        this.id = id; // Inicialización del ID

        this.tema = tema;
        this.urgencia = urgencia;
        this.fecha = fecha;
        this.estado = "Pendiente";
    }

    @Override
    public int compareTo(SolicitudAyuda otra) {
        return Integer.compare(otra.urgencia, this.urgencia); // Orden descendente
    }

    public String getId() {
        return id;
    }
}
