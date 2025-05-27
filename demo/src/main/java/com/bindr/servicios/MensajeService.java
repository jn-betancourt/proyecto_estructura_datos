package com.bindr.servicios;

import com.bindr.dao.ConversacionDao;
import com.bindr.dao.EstudianteDao;
import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.MensajeDTO;
import com.bindr.modelos.Conversacion;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.Mensaje;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la gestión de mensajes y conversaciones entre estudiantes.
 * Permite crear conversaciones, enviar mensajes, obtener conversaciones y eliminarlas.
 */
public class MensajeService {

    private static final AfinidadService afinidadService = new AfinidadService(); // Instancia o inyección adecuada

    /**
     * Obtiene todas las conversaciones en las que participa un usuario.
     * @param id Identificador del usuario (estudiante)
     * @return Lista de ConversacionDTO en las que participa el usuario
     */
    public static List<ConversacionDTO> obtenerConversacionesPorUsuario(Long id) {
        List<Conversacion> conversaciones = ConversacionDao.obtenerPorUsuarioId(id);
        return conversaciones.stream()
                .map(ConversacionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva conversación entre estudiantes.
     * @param correosParticipantes Lista de correos de los participantes
     * @param esGrupo Indica si la conversación es grupal
     * @return ConversacionDTO creada, o null si falla la creación
     */
    public static ConversacionDTO crearConversacion(List<String> correosParticipantes, boolean esGrupo) {
        List<Estudiante> participantes = correosParticipantes.stream()
                .map(EstudianteDao::buscarPorEmail)
                .filter(e -> e != null)
                .collect(Collectors.toList());

        if (participantes.size() < 2) {
            throw new IllegalArgumentException("Se necesitan al menos dos participantes válidos.");
        }

        // Registrar interacción entre todos los pares de participantes
        for (int i = 0; i < participantes.size(); i++) {
            for (int j = i + 1; j < participantes.size(); j++) {
                afinidadService.registrarInteraccion(participantes.get(i), participantes.get(j));
            }
        }

        Conversacion conversacion = Conversacion.builder()
                .participantes(participantes)
                .esGrupo(esGrupo)
                .fechaCreacion(LocalDateTime.now())
                .mensajes(List.of())
                .build();

        boolean creada = ConversacionDao.guardar(conversacion);
        if (creada) {
            return toDTO(conversacion);
        }
        return null;
    }

    /**
     * Envía un mensaje a una conversación existente.
     * @param conversacionId Identificador de la conversación
     * @param autorDTO DTO del autor del mensaje
     * @param contenido Contenido del mensaje
     * @return true si el mensaje se envió correctamente, false en caso contrario
     */
    public static boolean enviarMensaje(Long conversacionId, EstudianteDTO autorDTO, String contenido) {
        Conversacion conversacion = ConversacionDao.obtenerPorId(conversacionId);
        if (conversacion == null) return false;

        Estudiante autor = autorDTO.toEntity();
        if (autor == null) return false;

        // Registrar interacción entre autor y todos los demás participantes
        for (Estudiante participante : conversacion.getParticipantes()) {
            if (!participante.equals(autor)) {
                afinidadService.registrarInteraccion(autor, participante);
            }
        }

        Mensaje mensaje = Mensaje.builder()
                .autor(autor)
                .contenido(contenido)
                .fecha(LocalDateTime.now())
                .build();

        conversacion.getMensajes().add(mensaje);
        return ConversacionDao.guardar(conversacion);
    }

    /**
     * Obtiene una conversación en formato DTO por su identificador.
     * @param id Identificador de la conversación
     * @return ConversacionDTO correspondiente, o null si no existe
     */
    public static ConversacionDTO obtenerConversacionDTO(Long id) {
        Conversacion conversacion = ConversacionDao.obtenerPorId(id);
        if (conversacion == null) return null;
        return toDTO(conversacion);
    }

    /**
     * Elimina una conversación por su identificador.
     * @param id Identificador de la conversación
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public static boolean eliminarConversacion(Long id) {
        Conversacion conversacion = ConversacionDao.obtenerPorId(id);
        if (conversacion == null) return false;
        return ConversacionDao.eliminarConversacion(conversacion.getId());
    }

    // ---- Conversión a DTOs ----

    /**
     * Convierte una entidad Conversacion a su correspondiente DTO.
     * @param conversacion Entidad Conversacion
     * @return ConversacionDTO equivalente
     */
    private static ConversacionDTO toDTO(Conversacion conversacion) {
        List<EstudianteDTO> participantes = conversacion.getParticipantes().stream()
                .map(e -> new EstudianteDTO(e.getId(), e.getNombre(), e.getCorreo()))
                .collect(Collectors.toList());

        List<MensajeDTO> mensajes = conversacion.getMensajes().stream()
                .map(m -> {
                    Optional<Estudiante> autor = conversacion.getParticipantes().stream()
                            .filter(e -> e.getId().equals(m.getAutor().getId()))
                            .findFirst();
                    EstudianteDTO autorDTO = autor
                            .map(e -> new EstudianteDTO(e.getId(), e.getNombre(), e.getCorreo()))
                            .orElse(null);
                    return new MensajeDTO(autorDTO, m.getContenido(), m.getFecha());
                }).collect(Collectors.toList());

        return new ConversacionDTO(
                conversacion.getId(),
                conversacion.getFechaCreacion(),
                participantes,
                mensajes,
                conversacion.isEsGrupo()
        );
    }
}
