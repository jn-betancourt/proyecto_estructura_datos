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
import com.bindr.dto.ValoracionDTO;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.Publicacion;
import com.bindr.modelos.Valoracion;

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

        // castear DTO de valoraciones a entidades
        List<Valoracion> valoraciones = dto.valoraciones().stream()
                .map(ValoracionDTO::toEntity)
                .collect(Collectors.toList());

        Publicacion publicacion = new Publicacion.Builder()
                .publicador(publicador)
                .titulo(dto.titulo())
                .fecha(LocalDateTime.now())
                .materias(dto.materias())
                .valoraciones(valoraciones)
                .archivo(dto.archivo()) // nueva propiedad
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
            pub.getMaterias(),
            pub.getValoraciones().stream()
                .map(ValoracionDTO::fromEntity)
                .collect(Collectors.toList()),
            pub.getArchivo()
        );
    }

    // Eliminar publicación
    public static boolean eliminar(Long id) {
        return PublicacionDao.eliminar(id);
    }


    // Actualizar título y materias (como ejemplo)
    public static boolean actualizarPublicacion(PublicacionDTO dto) {
        Publicacion pub = PublicacionDao.buscarPorId(dto.id());
        if (pub == null) return false;

        pub.setTitulo(dto.titulo());
        pub.setMaterias(dto.materias());
        pub.setValoraciones(dto.valoraciones().stream()
                .map(ValoracionDTO::toEntity)
                .collect(Collectors.toList()));
        pub.setArchivo(dto.archivo()); // nueva propiedad
        pub.setFecha(LocalDateTime.now()); // actualizar fecha al momento de la edición


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
                            pub.getValoraciones().stream()
                                    .map(ValoracionDTO::fromEntity)
                                    .collect(Collectors.toList()),
                            pub.getArchivo()
                    );
                })
                .collect(Collectors.toList());
    }

}
