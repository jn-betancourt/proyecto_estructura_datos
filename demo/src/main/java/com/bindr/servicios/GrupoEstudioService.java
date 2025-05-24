package com.bindr.servicios;

import java.util.List;

import com.bindr.dao.ConversacionDao;
import com.bindr.dao.EstudianteDao;
import com.bindr.dao.GrupoEstudioDao;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.modelos.Conversacion;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.GrupoEstudio;

public class GrupoEstudioService {
    
    public boolean crearGrupo(GrupoEstudioDTO dto) {
        GrupoEstudio grupo = dto.toEntity();
        return GrupoEstudioDao.crear(grupo);
    }

    public boolean actualizarGrupo(GrupoEstudioDTO dto) {
        GrupoEstudio grupo = dto.toEntity();
        return GrupoEstudioDao.actualizar(grupo);
    }

    public boolean eliminarGrupo(Integer id) {
        return GrupoEstudioDao.eliminar(id);
    }

    public GrupoEstudioDTO obtenerPorId(Integer id) {
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(id);
        return grupo != null ? GrupoEstudioDTO.fromEntity(grupo) : null;
    }

    public List<GrupoEstudioDTO> listarTodos() {
        return GrupoEstudioDao.listarTodos().stream()
            .map(GrupoEstudioDTO::fromEntity)
            .toList();
    }

    public boolean agregarEstudianteAGrupo(Integer grupoId, Long estudianteId) {
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(grupoId);
        Estudiante estudiante = EstudianteDao.buscarPorId(estudianteId);
        if (grupo == null || estudiante == null) return false;

        grupo.agregarEstudiante(estudiante);
        return GrupoEstudioDao.actualizar(grupo);
    }

    public boolean removerEstudianteDeGrupo(Integer grupoId, Long estudianteId) {
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(grupoId);
        if (grupo == null) return false;

        grupo.getEstudiantes().removeIf(e -> e.getId().equals(estudianteId));
        return GrupoEstudioDao.actualizar(grupo);
    }

    public boolean asociarConversacion(Integer grupoId, Long conversacionId) {
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(grupoId);
        Conversacion conversacion = ConversacionDao.buscarPorId(conversacionId);
        if (grupo == null || conversacion == null) return false;

        grupo.setConversacion(conversacion);
        return GrupoEstudioDao.actualizar(grupo);
    }
}
