package com.bindr.modelos;

import java.util.*;


// Clase GrafoAfinidad
class GrafoAfinidad {
    private Map<Estudiante, List<Estudiante>> adyacencias;

    public GrafoAfinidad() {
        this.adyacencias = new HashMap<>();
    }

    public void agregarConexion(Estudiante estudiante1, Estudiante estudiante2) {
        adyacencias.computeIfAbsent(estudiante1, k -> new ArrayList<>()).add(estudiante2);
        adyacencias.computeIfAbsent(estudiante2, k -> new ArrayList<>()).add(estudiante1);
    }

    public List<Estudiante> sugerirCompañeros(Estudiante estudiante) {
        return adyacencias.getOrDefault(estudiante, new ArrayList<>());
    }
}
