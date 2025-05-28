package com.bindr.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

import com.bindr.dao.EstudianteDao;
import com.bindr.dao.PublicacionDao;
import com.bindr.dao.GrupoEstudioDao;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.dto.ValoracionDTO;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.Publicacion;
import com.bindr.modelos.Valoracion;
import com.bindr.modelos.GrupoEstudio;

/**
 * Servicio encargado de la gestión de publicaciones académicas.
 * Permite crear, actualizar, eliminar y consultar publicaciones, así como gestionar archivos adjuntos y valoraciones.
 */
public class PublicacionService {

    // Carpeta donde se almacenan los archivos adjuntos de las publicaciones
    private static final String CARPETA_ARCHIVOS = "publicaciones_archivos";
    private static final AfinidadService afinidadService = new AfinidadService(); // Instancia o inyección adecuada

    /**
     * Guarda un archivo recibido como InputStream en la carpeta de publicaciones.
     * @param archivoInput InputStream del archivo a guardar
     * @param nombreArchivo Nombre con el que se guardará el archivo
     * @return Ruta absoluta del archivo guardado
     * @throws IOException Si ocurre un error al guardar el archivo
     */
    public String guardarArchivo(InputStream archivoInput, String nombreArchivo) throws IOException {
        Path dir = Paths.get(CARPETA_ARCHIVOS);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        Path archivoDestino = dir.resolve(nombreArchivo);
        Files.copy(archivoInput, archivoDestino, StandardCopyOption.REPLACE_EXISTING);
        return archivoDestino.toAbsolutePath().toString();
    }

    /**
     * Crea una nueva publicación con un archivo adjunto.
     * @param dto DTO de la publicación
     * @param archivoInput InputStream del archivo adjunto
     * @param nombreArchivo Nombre del archivo adjunto
     * @return true si la publicación se creó correctamente, false en caso contrario
     */
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

    /**
     * Crea una nueva publicación sin archivo adjunto.
     * @param dto DTO de la publicación
     * @return true si la publicación se creó correctamente, false en caso contrario
     */
    public static boolean crearPublicacion(PublicacionDTO dto) {
        Estudiante publicador = EstudianteDao.buscarPorEmail(dto.publicador().correo());
        if (publicador == null) {
            return false;
        }

        // Convierte las valoraciones del DTO a entidades
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

    /**
     * Crea una publicación asociada a un grupo de estudio.
     */
    public static boolean crearPublicacionEnGrupo(PublicacionDTO dto, Long grupoId) {
        Estudiante publicador = EstudianteDao.buscarPorEmail(dto.publicador().correo());
        GrupoEstudio grupo = GrupoEstudioDao.buscarPorId(grupoId);
        if (publicador == null || grupo == null) {
            return false;
        }

        List<Valoracion> valoraciones = dto.valoraciones().stream()
                .map(ValoracionDTO::toEntity)
                .collect(Collectors.toList());

        Publicacion publicacion = new Publicacion.Builder()
                .publicador(publicador)
                .titulo(dto.titulo())
                .fecha(LocalDateTime.now())
                .materias(dto.materias())
                .valoraciones(valoraciones)
                .archivo(dto.archivo())
                .grupoEstudio(grupo)
                .build();

        // Relación bidireccional
        grupo.getPublicaciones().add(publicacion);
        publicacion.setGrupoEstudio(grupo);

        // Persistir publicación (JPA actualizará la relación)
        return PublicacionDao.crear(publicacion);
    }

    /**
     * Obtiene todas las publicaciones en formato DTO.
     * @return Lista de PublicacionDTO
     */
    public static List<PublicacionDTO> obtenerTodas() {
        List<Publicacion> publicaciones = PublicacionDao.listarTodas();
        return publicaciones.stream()
        .map(PublicacionDTO::fromEntity) // convierte cada publicación a DTO
        .collect(Collectors.toList());
    }

    /**
     * Busca una publicación por su identificador.
     * @param id Identificador de la publicación
     * @return PublicacionDTO correspondiente, o null si no existe
     */
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

    /**
     * Elimina una publicación por su identificador.
     * @param id Identificador de la publicación
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public static boolean eliminar(Long id) {
        return PublicacionDao.eliminar(id);
    }

    /**
     * Actualiza los datos de una publicación existente (título, materias, valoraciones, archivo).
     * @param dto DTO con los datos actualizados de la publicación
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public static boolean actualizarPublicacion(PublicacionDTO dto) {
        Publicacion pub = PublicacionDao.buscarPorId(dto.id());
        if (pub == null) return false;

        pub.setTitulo(dto.titulo());
        pub.setMaterias(dto.materias());
        pub.setValoraciones(dto.valoraciones().stream()
                .map(ValoracionDTO::toEntity)
                .collect(Collectors.toList()));
        pub.setArchivo(dto.archivo());
        pub.setFecha(LocalDateTime.now());

        // Registrar interacción por cada valoración nueva
        Estudiante autor = pub.getPublicador();
        for (ValoracionDTO val : dto.valoraciones()) {
            Estudiante evaluador = EstudianteDao.buscarPorEmail(val.autor());
            if (evaluador != null && !evaluador.equals(autor)) {
                afinidadService.registrarInteraccion(autor, evaluador);
            }
        }

        return PublicacionDao.actualizar(pub);
    }

    /**
     * Obtiene todas las publicaciones realizadas por un usuario específico.
     * @param idUsuario Identificador del usuario (estudiante)
     * @return Lista de PublicacionDTO correspondientes al usuario
     */
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
