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

public class MensajeService {

    public static List<ConversacionDTO> obtenerConversacionesPorUsuario(Long id) {
        List<Conversacion> conversaciones = ConversacionDao.obtenerPorUsuarioId(id);
        return conversaciones.stream()
                .map(ConversacionDTO::fromEntity)
                .collect(Collectors.toList());
    }
    // Crear una nueva conversación
    public static ConversacionDTO crearConversacion(List<String> correosParticipantes, boolean esGrupo) {
        List<Estudiante> participantes = correosParticipantes.stream()
                .map(EstudianteDao::buscarPorEmail)
                .filter(e -> e != null)
                .collect(Collectors.toList());

        if (participantes.size() < 2) {
            throw new IllegalArgumentException("Se necesitan al menos dos participantes válidos.");
        }

        Conversacion conversacion = Conversacion.builder()
        .participantes(participantes)
        .esGrupo(esGrupo)
        .fechaCreacion(LocalDateTime.now())
        .mensajes(List.of()).build();
        boolean creada = ConversacionDao.guardar(conversacion);
        if (creada) {
            return toDTO(conversacion);
        }
        return null;
    }

    // Enviar un mensaje a una conversación existente
    public static boolean enviarMensaje(Long conversacionId, String correoAutor, String contenido) {
        Conversacion conversacion = ConversacionDao.obtenerPorId(conversacionId);
        if (conversacion == null) return false;

        Estudiante autor = EstudianteDao.buscarPorEmail(correoAutor);
        if (autor == null) return false;

        Mensaje mensaje = Mensaje.builder()
                .autorId(autor.getId())
                .contenido(contenido)
                .fecha(LocalDateTime.now())
                .build();

        conversacion.getMensajes().add(mensaje);
        return ConversacionDao.guardar(conversacion);
    }

    // Obtener DTO de conversación por ID
    public static ConversacionDTO obtenerConversacionDTO(Long id) {
        Conversacion conversacion = ConversacionDao.obtenerPorId(id);
        System.out.println(conversacion.getParticipantes());
        if (conversacion == null) return null;
        return toDTO(conversacion);
    }

    // ---- Conversión a DTOs ----
    private static ConversacionDTO toDTO(Conversacion conversacion) {
        List<EstudianteDTO> participantes = conversacion.getParticipantes().stream()
                .map(e -> new EstudianteDTO(e.getId(), e.getNombre(), e.getCorreo()))
                .collect(Collectors.toList());

        List<MensajeDTO> mensajes = conversacion.getMensajes().stream()
                .map(m -> {
                    Optional<Estudiante> autor = conversacion.getParticipantes().stream()
                            .filter(e -> e.getId().equals(m.getAutorId()))
                            .findFirst();
                    EstudianteDTO autorDTO = autor
                            .map(e -> new EstudianteDTO(e.getId(), e.getNombre(), e.getCorreo()))
                            .orElse(null);
                    return new MensajeDTO(autorDTO, m.getContenido(), m.getFecha());
                }). collect(Collectors.toList());

        return new ConversacionDTO(
                conversacion.getId(),
                conversacion.getFechaCreacion(),
                participantes,
                mensajes,
                conversacion.isEsGrupo()
        );
    }
}
