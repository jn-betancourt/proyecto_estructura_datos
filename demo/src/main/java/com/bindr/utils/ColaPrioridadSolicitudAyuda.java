package com.bindr.utils;

import com.bindr.modelos.SolicitudAyuda;
import com.bindr.modelos.NivelUrgencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Estructura de datos personalizada para gestionar una cola de prioridad de solicitudes de ayuda académica.
 * Utiliza un heap binario para mantener siempre en la cima la solicitud más prioritaria,
 * considerando primero el nivel de urgencia y luego la fecha de creación.
 */
public class ColaPrioridadSolicitudAyuda {

    /**
     * Lista interna que representa el heap binario.
     * Cada elemento es una instancia de SolicitudAyuda.
     */
    private final List<SolicitudAyuda> heap;

    /**
     * Constructor: inicializa la lista interna vacía.
     */
    public ColaPrioridadSolicitudAyuda() {
        this.heap = new ArrayList<>();
    }

    /**
     * Compara dos solicitudes de ayuda para determinar su prioridad.
     * Primero compara el nivel de urgencia (ALTA > MEDIA > BAJA).
     * Si la urgencia es igual, prioriza la solicitud más antigua (fecha menor).
     *
     * @param a Primera solicitud a comparar.
     * @param b Segunda solicitud a comparar.
     * @return Valor positivo si 'a' tiene mayor prioridad, negativo si 'b', 0 si igual.
     */
    private int comparar(SolicitudAyuda a, SolicitudAyuda b) {
        int cmp = b.getUrgencia().compareTo(a.getUrgencia()); // ALTA > MEDIA > BAJA
        if (cmp == 0) {
            return a.getFecha().compareTo(b.getFecha()); // más antigua primero
        }
        return cmp;
    }

    /**
     * Inserta una nueva solicitud en la cola de prioridad.
     * La solicitud se ubica en la posición correcta para mantener la propiedad del heap.
     *
     * @param solicitud SolicitudAyuda a insertar.
     */
    public void insertar(SolicitudAyuda solicitud) {
        heap.add(solicitud);
        int i = heap.size() - 1;
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (comparar(heap.get(i), heap.get(padre)) > 0) {
                SolicitudAyuda temp = heap.get(i);
                heap.set(i, heap.get(padre));
                heap.set(padre, temp);
                i = padre;
            } else {
                break;
            }
        }
    }

    /**
     * Extrae y retorna la solicitud de mayor prioridad (la raíz del heap).
     * Reorganiza el heap después de la extracción.
     *
     * @return SolicitudAyuda de mayor prioridad, o null si la cola está vacía.
     */
    public SolicitudAyuda extraer() {
        if (heap.isEmpty()) return null;
        SolicitudAyuda max = heap.get(0);
        SolicitudAyuda ultimo = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, ultimo);
            heapify(0);
        }
        return max;
    }

    /**
     * Consulta la solicitud de mayor prioridad sin extraerla de la cola.
     *
     * @return SolicitudAyuda de mayor prioridad, o null si la cola está vacía.
     */
    public SolicitudAyuda peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    /**
     * Actualiza el nivel de urgencia de una solicitud específica (por id).
     * Reorganiza el heap para mantener la prioridad.
     *
     * @param id            Identificador de la solicitud a actualizar.
     * @param nuevaUrgencia Nuevo nivel de urgencia.
     * @return true si se actualizó, false si no se encontró la solicitud.
     */
    public boolean actualizarPrioridad(Integer id, NivelUrgencia nuevaUrgencia) {
        for (int i = 0; i < heap.size(); i++) {
            SolicitudAyuda s = heap.get(i);
            if (s.getId() != null && s.getId().equals(id)) {
                s.setUrgencia(nuevaUrgencia);
                heapify(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina una solicitud de la cola por su identificador.
     * Reorganiza el heap si es necesario.
     *
     * @param id Identificador de la solicitud a eliminar.
     * @return true si se eliminó, false si no se encontró.
     */
    public boolean eliminar(Integer id) {
        for (int i = 0; i < heap.size(); i++) {
            SolicitudAyuda s = heap.get(i);
            if (s.getId() != null && s.getId().equals(id)) {
                heap.remove(i);
                if (i < heap.size()) heapify(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve una lista de todas las solicitudes en la cola, ordenadas por prioridad.
     * No modifica la estructura interna del heap.
     *
     * @return Lista ordenada de SolicitudAyuda.
     */
    public List<SolicitudAyuda> listar() {
        List<SolicitudAyuda> copia = new ArrayList<>(heap);
        copia.sort(this::comparar);
        return copia;
    }

    /**
     * Reorganiza el heap a partir de la posición dada para mantener la propiedad de heap.
     *
     * @param i Índice desde el cual reorganizar.
     */
    private void heapify(int i) {
        int izq = 2 * i + 1;
        int der = 2 * i + 2;
        int mayor = i;
        if (izq < heap.size() && comparar(heap.get(izq), heap.get(mayor)) > 0) mayor = izq;
        if (der < heap.size() && comparar(heap.get(der), heap.get(mayor)) > 0) mayor = der;
        if (mayor != i) {
            SolicitudAyuda temp = heap.get(i);
            heap.set(i, heap.get(mayor));
            heap.set(mayor, temp);
            heapify(mayor);
        }
    }

    /**
     * Elimina todas las solicitudes de la cola de prioridad, dejándola vacía.
     */
    public void vaciar() {
        heap.clear();
    }
}