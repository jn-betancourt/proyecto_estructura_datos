package com.bindr.modelos;

import java.util.*;


// Clase GrupoEstudio
class GrupoEstudio {
    private String id;
    private String tema;
    private List<Estudiante> estudiantes;
    private List<ContenidoEducativo> contenidosRelacionados;

    public GrupoEstudio(String id, String tema) {
        this.id = id;
        this.tema = tema;
        this.estudiantes = new ArrayList<>();
        this.contenidosRelacionados = new ArrayList<>();
    }

    public void agregarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }
}