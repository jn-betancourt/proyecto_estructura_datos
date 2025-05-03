package com.bindr.modelos;

import java.util.*;


// Clase Mensaje
class Mensaje {
    private String id; // Nuevo campo

    private Estudiante remitente;
    private Estudiante destinatario;
    private String contenido;
    private Date fecha;
    private boolean leido;

    public Mensaje(String id, Estudiante remitente, Estudiante destinatario, String contenido, Date fecha) {
        this.id = id; // Inicialización del ID

        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fecha = fecha;
        this.leido = false;
    }

    public void marcarComoLeido() {
        this.leido = true;
    }

    public String getId() {
        return id;
    }
}