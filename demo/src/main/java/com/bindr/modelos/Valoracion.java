package com.bindr.modelos;

import java.util.*;


// Clase Valoracion
class Valoracion {
    private String id; // Nuevo campo

    private ContenidoEducativo contenido;
    private int puntuacion;
    private String comentario;
    private Date fecha;

    public Valoracion(String id,ContenidoEducativo contenido, int puntuacion, String comentario, Date fecha) {
        this.id = id; // Inicialización del ID

        this.contenido = contenido;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = fecha;
    }
    public String getId() {
        return id;
    }
}