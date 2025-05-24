package com.bindr.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.*;
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

    private static final String CARPETA_ARCHIVOS = "publicaciones_archivos";

    public String guardarArchivo(InputStream archivoInput, String nombreArchivo) throws IOException {
        Path dir = Paths.get(CARPETA_ARCHIVOS);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        Path archivoDestino = dir.resolve(nombreArchivo);
        Files.copy(archivoInput, archivoDestino, StandardCopyOption.REPLACE_EXISTING);
        return archivoDestino.toAbsolutePath().toString();
    }

    public boolean crearPublicacionConArchivo(PublicacionDTO dto, InputStream archivoInput, String nombreArchivo) {
        try {
            String uriArchivo = guardarArchivo(archivoInput, nombreArchivo);
            Publicacion publicacion = dto.toEntity();
            publicacion.setArchivo(uriArchivo);
            return PublicacionDao.crear(publicacion);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

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
            pub.getMaterias(), pub.getArchivo()
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

    public static List<PublicacionDTO> obtenerPorIdDeUsuario(Long idUsuario) {
        List<Publicacion> publicaciones = PublicacionDao.buscarPorPublicadorId(idUsuario);

        return publicaciones.stream()
                .map(pub -> {
                    Estudiante est = pub.getPublicador();
                    EstudianteDTO estDto = new EstudianteDTO(est.getId(), est.getNombre(), est.getCorreo());
                    return new PublicacionDTO(
                            pub.getId(),
                            estDto,
                            pub.getFecha(),
                            pub.getTitulo(),
                            pub.getMaterias(),
                            pub.getArchivo()
                    );
                })
                .collect(Collectors.toList());
    }

}
