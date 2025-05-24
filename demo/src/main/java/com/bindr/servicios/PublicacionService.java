package com.bindr.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.bindr.dao.EstudianteDao;
import com.bindr.dao.PublicacionDao;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.modelos.Publicacion;

public class PublicacionService {

    // Crear nueva publicación
    public static boolean crearPublicacion(PublicacionDTO dto) {
        Estudiante publicador = EstudianteDao.buscarPorEmail(dto.publicador().correo());
        if (publicador == null) {
            return false;
        }

        Publicacion publicacion = new Publicacion.Builder()
                .publicador(publicador)
                .titulo(dto.titulo())
                .fecha(LocalDateTime.now())
                .materias(dto.materias())
                .build();

        return PublicacionDao.crear(publicacion);
    }

    // Obtener todas las publicaciones en formato DTO
    public static List<PublicacionDTO> obtenerTodas() {
        List<Publicacion> publicaciones = PublicacionDao.listarTodas();
        return publicaciones.stream()
        .map(PublicacionDTO::fromEntity) // convierte cada publicación a DTO
        .collect(Collectors.toList());
    }

    // Buscar publicación por ID
    public static PublicacionDTO buscarPorId(Long id) {
        Publicacion pub = PublicacionDao.buscarPorId(id);
        if (pub == null) return null;

        Estudiante est = pub.getPublicador();
        EstudianteDTO estDto = new EstudianteDTO(est.getId(), est.getNombre(), est.getCorreo());

        return new PublicacionDTO(
            pub.getId(),
            estDto,
            pub.getFecha(),
            pub.getTitulo(),
            pub.getMaterias()
        );
    }

    // Eliminar publicación
    public static boolean eliminar(Long id) {
        return PublicacionDao.eliminar(id);
    }

    // Actualizar título y materias (como ejemplo)
    public static boolean actualizarPublicacion(Long id, String nuevoTitulo, List<MateriaEstudio> nuevasMaterias) {
        Publicacion pub = PublicacionDao.buscarPorId(id);
        if (pub == null) return false;

        pub.setTitulo(nuevoTitulo);
        pub.setMaterias(nuevasMaterias);

        return PublicacionDao.actualizar(pub);
    }
}
