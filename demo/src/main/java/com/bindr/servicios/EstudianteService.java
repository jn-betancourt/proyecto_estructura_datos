package com.bindr.servicios;

import com.bindr.dao.EstudianteDao;
import com.bindr.dao.PublicacionDao;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.dto.ConversacionDTO;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.Publicacion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la gestión de estudiantes y sus operaciones relacionadas.
 * Proporciona métodos para buscar, actualizar y eliminar estudiantes,
 * así como para obtener sus publicaciones y conversaciones.
 */
public class EstudianteService {

    /**
     * Busca un estudiante por su identificador único.
     * @param id Identificador del estudiante
     * @return EstudianteDTO correspondiente, o null si no existe
     */
    public EstudianteDTO buscarPorId(Long id) {
        Estudiante estudiante = EstudianteDao.buscarPorId(id);
        return EstudianteDTO.fromEntity(estudiante);
    }

    /**
     * Busca un estudiante por su correo electrónico.
     * @param correo Correo electrónico del estudiante
     * @return EstudianteDTO correspondiente, o null si no existe
     */
    public EstudianteDTO buscarPorCorreo(String correo) {
        Estudiante estudiante = EstudianteDao.buscarPorEmail(correo);
        return EstudianteDTO.fromEntity(estudiante);
    }

    /**
     * Actualiza los datos de un estudiante (excepto la contraseña).
     * @param dto DTO con los datos actualizados del estudiante
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarEstudiante(EstudianteDTO dto) {
        Estudiante estudiante = dto.toEntity();
        return EstudianteDao.actualizarEstudiante(estudiante);
    }

    /**
     * Obtiene todas las publicaciones realizadas por un estudiante.
     * @param estudianteId Identificador del estudiante
     * @return Lista de PublicacionDTO correspondientes al estudiante
     */
    public List<PublicacionDTO> obtenerPublicaciones(Long estudianteId) {
        List<Publicacion> publicaciones = PublicacionDao.listarTodas().stream()
            .filter(p -> p.getPublicador() != null && p.getPublicador().getId().equals(estudianteId))
            .collect(Collectors.toList());

        return publicaciones.stream()
            .map(PublicacionDTO::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista de todos los estudiantes registrados en el sistema.
     * @return Lista de EstudianteDTO
     */
    public List<EstudianteDTO> listarEstudiantes() {
        return EstudianteDao.listarTodos().stream()
            .map(EstudianteDTO::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Elimina un estudiante del sistema por su identificador.
     * @param id Identificador del estudiante a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    public boolean eliminarEstudiante(Long id) {
        Estudiante estudiante = EstudianteDao.buscarPorId(id);
        if (estudiante == null) return false;
        return EstudianteDao.eliminarEstudiante(estudiante.getId());
    }

    /**
     * Obtiene todas las conversaciones en las que participa un estudiante.
     * @param estudianteId Identificador del estudiante
     * @return Lista de ConversacionDTO correspondientes al estudiante
     */
    public List<ConversacionDTO> obtenerConversaciones(Long estudianteId) {
        Estudiante estudiante = EstudianteDao.buscarPorId(estudianteId);
        if (estudiante == null) return List.of();

        return estudiante.getConversaciones().stream()
            .map(ConversacionDTO::fromEntity)
            .collect(Collectors.toList());
    }
}