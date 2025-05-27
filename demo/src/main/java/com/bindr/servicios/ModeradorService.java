package com.bindr.servicios;

import com.bindr.dao.ModeradorDao;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.dto.ModeradorDTO;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.Moderador;
import com.bindr.modelos.Publicacion;

import java.util.*;
import java.util.stream.Collectors;

public class ModeradorService {

    private final EstudianteService estudianteService = new EstudianteService();
    private final GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
    private final AfinidadService afinidadService = new AfinidadService();
    private final ModeradorDao moderadorDao = new ModeradorDao();

    // CRUD Moderador
    public void crearModerador(ModeradorDTO dto) {
        moderadorDao.crear(dto.toEntity());
    }

    public ModeradorDTO buscarModeradorPorId(Long id) {
        Moderador mod = moderadorDao.buscarPorId(id.intValue());
        return (mod != null) ? ModeradorDTO.fromEntity(mod) : null;
    }

    public ModeradorDTO buscarModeradorPorCorreo(String correo) {
        Moderador mod = moderadorDao.buscarPorCorreo(correo);
        return (mod != null) ? ModeradorDTO.fromEntity(mod) : null;
    }

    public List<ModeradorDTO> listarModeradores() {
        return moderadorDao.listarTodos().stream()
                .map(ModeradorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void actualizarModerador(ModeradorDTO dto) {
        moderadorDao.actualizar(dto.toEntity());
    }

    public void eliminarModerador(Long id) {
        moderadorDao.eliminar(id.intValue());
    }

    // 1. Listar usuarios
    public List<EstudianteDTO> listarUsuarios() {
        return estudianteService.listarEstudiantes();
    }

    // 2. Listar grupos
    public List<GrupoEstudioDTO> listarGrupos() {
        return grupoEstudioService.listarTodos();
    }

    // 3. Visualizar grafo de afinidad (estructura completa)
    public Map<EstudianteDTO, Map<EstudianteDTO, Integer>> obtenerGrafoAfinidad() {
        // Devuelve el mapa de adyacencias completo usando DTOs
        return afinidadService.obtenerTodosEstudiantes().stream()
                .collect(Collectors.toMap(
                        EstudianteDTO::fromEntity,
                        e -> afinidadService.obtenerAfinidades(e).entrySet().stream()
                                .collect(Collectors.toMap(
                                        entry -> EstudianteDTO.fromEntity(entry.getKey()),
                                        Map.Entry::getValue
                                ))
                ));
    }

    // 4. Reporte: Contenidos más valorados
    public List<Publicacion> obtenerContenidosMasValorados(int topN) {
        return com.bindr.dao.PublicacionDao.listarTodas().stream()
                .sorted((p1, p2) -> Integer.compare(
                        p2.getValoraciones().size(),
                        p1.getValoraciones().size()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    // 5. Reporte: Estudiantes con más conexiones
    public List<EstudianteDTO> obtenerEstudiantesConMasConexiones(int topN) {
        return afinidadService.obtenerTodosEstudiantes().stream()
                .sorted((e1, e2) -> Integer.compare(
                        afinidadService.obtenerAfinidades(e2).size(),
                        afinidadService.obtenerAfinidades(e1).size()))
                .limit(topN)
                .map(EstudianteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 6. Reporte: Caminos más cortos entre dos estudiantes (requiere implementación en GrafoAfinidadEstudiantes)
    public List<EstudianteDTO> obtenerCaminoMasCorto(EstudianteDTO origen, EstudianteDTO destino) {
        List<Estudiante> camino = afinidadService.getGrafo().caminoMasCorto(origen.toEntity(), destino.toEntity());
        return camino.stream().map(EstudianteDTO::fromEntity).collect(Collectors.toList());
    }

    // 7. Reporte: Detección de comunidades de estudio (clústeres)
    public List<Set<EstudianteDTO>> detectarComunidades() {
        List<Set<Estudiante>> comunidades = afinidadService.getGrafo().detectarComunidades();
        return comunidades.stream()
                .map(set -> set.stream().map(EstudianteDTO::fromEntity).collect(Collectors.toSet()))
                .collect(Collectors.toList());
    }

    // 8. Reporte: Niveles de participación (por cantidad de interacciones)
    public Map<EstudianteDTO, Integer> obtenerNivelesParticipacion() {
        Map<EstudianteDTO, Integer> participacion = new HashMap<>();
        for (Estudiante e : afinidadService.obtenerTodosEstudiantes()) {
            int total = afinidadService.obtenerAfinidades(e).values().stream().mapToInt(Integer::intValue).sum();
            participacion.put(EstudianteDTO.fromEntity(e), total);
        }
        return participacion;
    }
}