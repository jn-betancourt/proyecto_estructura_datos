package com.bindr.utils;

import com.bindr.modelos.Estudiante;

import java.util.*;

/**
 * Estructura de datos para representar un grafo no dirigido de afinidad entre estudiantes.
 * Cada nodo es un estudiante y cada arista representa una relación de afinidad (colaboración, mensajes, etc.).
 * Las aristas pueden tener un peso que indica el nivel de afinidad.
 */
public class GrafoAfinidadEstudiantes {

    // Mapa de cada estudiante a su lista de conexiones (adyacentes) y el peso de la relación
    private final Map<Estudiante, Map<Estudiante, Integer>> adyacencias = new HashMap<>();

    /**
     * Agrega un estudiante (nodo) al grafo.
     * Si ya existe, no hace nada.
     * @param estudiante Estudiante a agregar
     */
    public void agregarEstudiante(Estudiante estudiante) {
        adyacencias.putIfAbsent(estudiante, new HashMap<>());
    }

    /**
     * Crea o refuerza una relación de afinidad entre dos estudiantes.
     * Si la relación ya existe, incrementa el peso.
     * @param e1 Primer estudiante
     * @param e2 Segundo estudiante
     */
    public void agregarRelacion(Estudiante e1, Estudiante e2) {
        agregarEstudiante(e1);
        agregarEstudiante(e2);
        // Incrementa el peso de la relación en ambos sentidos (no dirigido)
        adyacencias.get(e1).put(e2, adyacencias.get(e1).getOrDefault(e2, 0) + 1);
        adyacencias.get(e2).put(e1, adyacencias.get(e2).getOrDefault(e1, 0) + 1);
    }

    /**
     * Obtiene los estudiantes conectados (adyacentes) a un estudiante dado.
     * @param estudiante Estudiante de referencia
     * @return Mapa de estudiantes adyacentes y el peso de la relación
     */
    public Map<Estudiante, Integer> obtenerAdyacentes(Estudiante estudiante) {
        return adyacencias.getOrDefault(estudiante, Collections.emptyMap());
    }

    /**
     * Obtiene todos los estudiantes presentes en el grafo.
     * @return Conjunto de estudiantes (nodos)
     */
    public Set<Estudiante> obtenerEstudiantes() {
        return adyacencias.keySet();
    }

    /**
     * Verifica si dos estudiantes están conectados.
     * @param e1 Primer estudiante
     * @param e2 Segundo estudiante
     * @return true si existe una relación, false en caso contrario
     */
    public boolean estanConectados(Estudiante e1, Estudiante e2) {
        return adyacencias.containsKey(e1) && adyacencias.get(e1).containsKey(e2);
    }

    public void vaciar() {
        adyacencias.clear();
    }
}
