package com.bindr.modelos;

import java.util.*;


// Clase ColaPrioridad
class ColaPrioridad {
    private PriorityQueue<SolicitudAyuda> solicitudes;

    public ColaPrioridad() {
        this.solicitudes = new PriorityQueue<>();
    }

    public void insertarSolicitud(SolicitudAyuda solicitud) {
        solicitudes.add(solicitud);
    }

    public SolicitudAyuda atenderSolicitud() {
        return solicitudes.poll();
    }
}
