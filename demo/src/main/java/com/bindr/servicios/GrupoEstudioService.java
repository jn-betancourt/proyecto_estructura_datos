package com.bindr.servicios;

import java.util.List;

import com.bindr.dao.ConversacionDao;
import com.bindr.dao.EstudianteDao;
import com.bindr.dao.GrupoEstudioDao;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.modelos.Conversacion;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.GrupoEstudio;

/**
 * Servicio encargado de la gestión de grupos de estudio.
 * Permite crear, actualizar, eliminar y consultar grupos, así como gestionar la asociación de estudiantes y conversaciones.
 */
public class GrupoEstudioService {
    
    /**
     * Crea un nuevo grupo de estudio a partir de un DTO.
     * @param dto DTO con la información del grupo a crear
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearGrupo(GrupoEstudioDTO dto) {
        GrupoEstudio grupo = dto.toEntity();
        return GrupoEstudioDao.crear(grupo);
    }

    /**
     * Actualiza la información de un grupo de estudio existente.
     * @param dto DTO con los datos actualizados del grupo
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarGrupo(GrupoEstudioDTO dto) {
        GrupoEstudio grupo = dto.toEntity();
        return GrupoEstudioDao.actualizar(grupo);
    }

    /**
     * Elimina un grupo de estudio por su identificador.
     * @param id Identificador del grupo a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarGrupo(Integer id) {
        return GrupoEstudioDao.eliminar(id);
    }

    /**
     * Obtiene un grupo de estudio por su identificador.
     * @param id Identificador del grupo
     * @return GrupoEstudioDTO correspondiente, o null si no existe
     */
    public GrupoEstudioDTO obtenerPorId(Integer id) {
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(id);
        return grupo != null ? GrupoEstudioDTO.fromEntity(grupo) : null;
    }

    /**
     * Lista todos los grupos de estudio existentes.
     * @return Lista de GrupoEstudioDTO
     */
    public List<GrupoEstudioDTO> listarTodos() {
        return GrupoEstudioDao.listarTodos().stream()
            .map(GrupoEstudioDTO::fromEntity)
            .toList();
    }

    /**
     * Agrega un estudiante a un grupo de estudio.
     * @param grupoId Identificador del grupo
     * @param estudianteId Identificador del estudiante
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarEstudianteAGrupo(Integer grupoId, Long estudianteId) {
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(grupoId);
        Estudiante estudiante = EstudianteDao.buscarPorId(estudianteId);
        if (grupo == null || estudiante == null) return false;

        grupo.agregarEstudiante(estudiante);
        return GrupoEstudioDao.actualizar(grupo);
    }

    /**
     * Remueve un estudiante de un grupo de estudio.
     * @param grupoId Identificador del grupo
     * @param estudianteId Identificador del estudiante
     * @return true si se removió correctamente, false en caso contrario
     */
    public boolean removerEstudianteDeGrupo(Integer grupoId, Long estudianteId) {
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(grupoId);
        if (grupo == null) return false;

        grupo.getEstudiantes().removeIf(e -> e.getId().equals(estudianteId));
        return GrupoEstudioDao.actualizar(grupo);
    }

    /**
     * Asocia una conversación a un grupo de estudio.
     * @param grupoId Identificador del grupo
     * @param conversacionId Identificador de la conversación
     * @return true si se asoció correctamente, false en caso contrario
     */
    public boolean asociarConversacion(Integer grupoId, Long conversacionId) {
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(grupoId);
        Conversacion conversacion = ConversacionDao.buscarPorId(conversacionId);
        if (grupo == null || conversacion == null) return false;

        grupo.setConversacion(conversacion);
        return GrupoEstudioDao.actualizar(grupo);
    }
}
