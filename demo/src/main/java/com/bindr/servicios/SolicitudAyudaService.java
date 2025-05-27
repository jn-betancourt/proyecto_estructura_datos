package com.bindr.servicios;

import com.bindr.dao.SolicitudAyudaDao;
import com.bindr.utils.ColaPrioridadSolicitudAyuda;
import com.bindr.modelos.SolicitudAyuda;
import com.bindr.dto.SolicitudAyudaDTO;
import com.bindr.dto.EstudianteDTO;

import java.util.List;

/**
 * Servicio para la gestión de solicitudes de ayuda académica,
 * integrando la estructura de cola de prioridad para el ordenamiento por urgencia y fecha.
 */
public class SolicitudAyudaService {

    // Cola de prioridad en memoria para gestionar el orden de atención
    private final ColaPrioridadSolicitudAyuda colaPrioridad = new ColaPrioridadSolicitudAyuda();

    /**
     * Crea una nueva solicitud de ayuda y la inserta en la cola de prioridad y la base de datos.
     * @param dto Objeto de transferencia de datos con la información de la solicitud a crear
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearSolicitud(SolicitudAyudaDTO dto) {
        SolicitudAyuda solicitud = dto.toEntity();
        boolean creada = SolicitudAyudaDao.crear(solicitud);
        if (creada) {
            colaPrioridad.insertar(solicitud);
        }
        return creada;
    }

    /**
     * Actualiza una solicitud (por ejemplo, al ser aceptada o resuelta) y actualiza la cola de prioridad.
     * @param dto Objeto de transferencia de datos con la información de la solicitud a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarSolicitud(SolicitudAyudaDTO dto) {
        SolicitudAyuda solicitud = dto.toEntity();
        boolean actualizada = SolicitudAyudaDao.actualizar(solicitud);
        if (actualizada) {
            // Si cambia la urgencia, actualiza la prioridad en la cola
            colaPrioridad.actualizarPrioridad(solicitud.getId(), solicitud.getUrgencia());
        }
        return actualizada;
    }

    /**
     * Elimina una solicitud de ayuda de la base de datos y de la cola de prioridad.
     * @param id Identificador de la solicitud
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarSolicitud(Integer id) {
        boolean eliminada = SolicitudAyudaDao.eliminar(id);
        if (eliminada) {
            colaPrioridad.eliminar(id);
        }
        return eliminada;
    }

    /**
     * Obtiene la lista de solicitudes ordenadas por prioridad (urgencia y fecha).
     * @return Lista de SolicitudAyudaDTO ordenada
     */
    public List<SolicitudAyudaDTO> listarSolicitudesPorPrioridad() {
        // Refresca la cola con todas las solicitudes actuales de la base de datos
        colaPrioridadReset();
        return colaPrioridad.listar().stream()
            .map(SolicitudAyudaDTO::fromEntity)
            .toList();
    }

    /**
     * Obtiene la solicitud más prioritaria (más urgente y antigua).
     * @return SolicitudAyudaDTO de mayor prioridad, o null si no hay solicitudes
     */
    public SolicitudAyudaDTO obtenerSolicitudMasPrioritaria() {
        colaPrioridadReset();
        SolicitudAyuda solicitud = colaPrioridad.peek();
        return solicitud != null ? SolicitudAyudaDTO.fromEntity(solicitud) : null;
    }

    /**
     * Cambia el estado de una solicitud a "aceptada" y asigna el destinatario.
     * @param idSolicitud ID de la solicitud a aceptar
     * @param destinatario DTO del estudiante que acepta la solicitud
     * @return SolicitudAyudaDTO actualizada, o null si no se pudo actualizar
     */
    public SolicitudAyudaDTO aceptarSolicitud(Integer idSolicitud, EstudianteDTO destinatario) {
        SolicitudAyuda solicitud = SolicitudAyudaDao.buscarPorId(idSolicitud);
        if (solicitud == null || !"pendiente".equalsIgnoreCase(solicitud.getEstado())) return null;
        solicitud.setDestinatario(destinatario.toEntity());
        solicitud.setEstado("aceptada");
        boolean ok = SolicitudAyudaDao.actualizar(solicitud);
        if (ok) {
            colaPrioridad.actualizarPrioridad(solicitud.getId(), solicitud.getUrgencia());
            return SolicitudAyudaDTO.fromEntity(solicitud);
        }
        return null;
    }

    /**
     * Cambia el estado de una solicitud a "resuelta".
     * @param idSolicitud ID de la solicitud a resolver
     * @return SolicitudAyudaDTO actualizada, o null si no se pudo actualizar
     */
    public SolicitudAyudaDTO resolverSolicitud(Integer idSolicitud) {
        SolicitudAyuda solicitud = SolicitudAyudaDao.buscarPorId(idSolicitud);
        if (solicitud == null || !"aceptada".equalsIgnoreCase(solicitud.getEstado())) return null;
        solicitud.setEstado("resuelta");
        boolean ok = SolicitudAyudaDao.actualizar(solicitud);
        if (ok) {
            colaPrioridad.eliminar(solicitud.getId());
            return SolicitudAyudaDTO.fromEntity(solicitud);
        }
        return null;
    }

    /**
     * Cambia el estado de una solicitud a "cancelada".
     * @param idSolicitud ID de la solicitud a cancelar
     * @return SolicitudAyudaDTO actualizada, o null si no se pudo actualizar
     */
    public SolicitudAyudaDTO cancelarSolicitud(Integer idSolicitud) {
        SolicitudAyuda solicitud = SolicitudAyudaDao.buscarPorId(idSolicitud);
        if (solicitud == null || "resuelta".equalsIgnoreCase(solicitud.getEstado())) return null;
        solicitud.setEstado("cancelada");
        boolean ok = SolicitudAyudaDao.actualizar(solicitud);
        if (ok) {
            colaPrioridad.eliminar(solicitud.getId());
            return SolicitudAyudaDTO.fromEntity(solicitud);
        }
        return null;
    }

    /**
     * Refresca la cola de prioridad con las solicitudes actuales de la base de datos.
     * Esto asegura que la cola esté sincronizada con la información persistente.
     */
    private void colaPrioridadReset() {
        colaPrioridad.vaciar();
        List<SolicitudAyuda> todas = SolicitudAyudaDao.obtenerTodas();
        for (SolicitudAyuda s : todas) {
            colaPrioridad.insertar(s);
        }
    }
}