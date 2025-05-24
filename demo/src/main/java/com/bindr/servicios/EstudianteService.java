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

public class EstudianteService {

    // Buscar estudiante por ID
    public EstudianteDTO buscarPorId(Long id) {
        Estudiante estudiante = EstudianteDao.buscarPorId(id);
        return EstudianteDTO.fromEntity(estudiante);
    }

    // Buscar estudiante por correo
    public EstudianteDTO buscarPorCorreo(String correo) {
        Estudiante estudiante = EstudianteDao.buscarPorEmail(correo);
        return EstudianteDTO.fromEntity(estudiante);
    }

    // Actualizar datos del estudiante (excepto contraseña por ahora)
    public boolean actualizarEstudiante(EstudianteDTO dto) {
        Estudiante estudiante = dto.toEntity();
        return EstudianteDao.actualizarEstudiante(estudiante);
    }

    // Obtener publicaciones del estudiante
    public List<PublicacionDTO> obtenerPublicaciones(Long estudianteId) {
        List<Publicacion> publicaciones = PublicacionDao.listarTodas().stream()
            .filter(p -> p.getPublicador() != null && p.getPublicador().getId().equals(estudianteId))
            .collect(Collectors.toList());

        return publicaciones.stream()
            .map(PublicacionDTO::fromEntity)
            .collect(Collectors.toList());
    }

    // Obtener conversaciones del estudiante
    public List<ConversacionDTO> obtenerConversaciones(Long estudianteId) {
        Estudiante estudiante = EstudianteDao.buscarPorId(estudianteId);
        if (estudiante == null) return List.of();

        return estudiante.getConversaciones().stream()
            .map(ConversacionDTO::fromEntity)
            .collect(Collectors.toList());
    }
}

