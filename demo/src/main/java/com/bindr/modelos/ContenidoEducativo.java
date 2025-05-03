package com.bindr.modelos;

import java.util.*;


// Clase ContenidoEducativo
class ContenidoEducativo {
    private String id;
    private String titulo;
    private Estudiante autor;
    private String tipo;
    private Date fechaPublicacion;
    private List<Valoracion> valoraciones;

    public ContenidoEducativo(String id, String titulo, Estudiante autor, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.tipo = tipo;
        this.fechaPublicacion = new Date();
        this.valoraciones = new ArrayList<>();
    }

    public void agregarValoracion(Valoracion valoracion) {
        valoraciones.add(valoracion);
    }
}