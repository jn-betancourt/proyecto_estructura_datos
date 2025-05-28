package com.bindr;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.EstudianteService;
import com.bindr.servicios.GrupoEstudioService;
import com.bindr.servicios.MensajeService;
import com.bindr.servicios.PublicacionService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class GrupoEstudioIntegracionTest {

    private final String correo1 = "correo1@gmail.com";
    private final String correo2 = "correo2@gmail.com";
    private EstudianteDTO estudiante1;
    private EstudianteDTO estudiante2;

    private void crearUsuariosSiNoExisten() {
        AutenticacionService estudianteService = new AutenticacionService();
        estudiante1 = estudianteService.registrar(new RegistroRequestDTO("usuario1", correo1, "pass1"));
        estudiante2 = estudianteService.registrar(new RegistroRequestDTO("usuario2", correo2, "pass1"));
        if (estudiante1 == null) {
            estudiante1 = estudianteService.registrar(new RegistroRequestDTO("usuario1", correo1, "pass1"));
        }
        if (estudiante2 == null) {
            estudiante2 = estudianteService.registrar(new RegistroRequestDTO("usuario2", correo2, "pass1"));
        }
    }

    @Test
    public void testFlujoCompletoGrupoEstudio() {
        crearUsuariosSiNoExisten();

        // Crear grupo
        GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
        GrupoEstudioDTO grupo = new GrupoEstudioDTO(
            null,
            "Grupo de Prueba",
            List.of(MateriaEstudio.BIOLOGIA),
            List.of(estudiante1, estudiante2),
            null,
            List.of()
        );
        GrupoEstudioDTO grupoEstudio = grupoEstudioService.crearGrupo(grupo);

        Assertions.assertNotNull(grupoEstudio.id(), "El grupo no fue creado correctamente");
        Assertions.assertEquals("Grupo de Prueba", grupoEstudio.nombre());

        // Asociar conversación
        MensajeService conversacionService = new MensajeService();
        List<String> participantes = Arrays.asList(estudiante1.correo(), estudiante2.correo());
        ConversacionDTO conversacion = conversacionService.crearConversacion(participantes, true);

        boolean asociado = grupoEstudioService.asociarConversacion(grupoEstudio.id(), conversacion.id());
        GrupoEstudioDTO grupoActualizado = grupoEstudioService.obtenerPorId(grupoEstudio.id());

        Assertions.assertTrue(asociado, "No se pudo asociar la conversación al grupo");
        Assertions.assertNotNull(grupoActualizado.conversacion());
        Assertions.assertTrue(grupoActualizado.conversacion().esGrupo());
        Assertions.assertEquals(conversacion.id(), grupoActualizado.conversacion().id());

        // Agregar publicación al grupo
        PublicacionDTO publicacionDTO = new PublicacionDTO(
            null,
            estudiante1,
            LocalDateTime.now(),
            "Título de prueba",
            List.of(MateriaEstudio.BIOLOGIA),
            List.of(),
            null
        );
        boolean creada = PublicacionService.crearPublicacionEnGrupo(publicacionDTO, grupoEstudio.id());
        Assertions.assertTrue(creada, "No se pudo crear la publicación en el grupo");

        List<PublicacionDTO> publicaciones = grupoEstudioService.obtenerPublicacionesDeGrupo(grupoEstudio.id());
        Assertions.assertFalse(publicaciones.isEmpty(), "El grupo no tiene publicaciones asociadas");
        Assertions.assertEquals("Título de prueba", publicaciones.get(0).titulo());

        // Verificar participantes del grupo
        GrupoEstudioDTO grupoGuardado = grupoEstudioService.obtenerPorId(grupoEstudio.id());
        List<String> correosEsperados = List.of(estudiante1.correo(), estudiante2.correo());
        List<String> correosEnGrupo = grupoGuardado.estudiantes().stream()
            .map(EstudianteDTO::correo)
            .toList();

        Assertions.assertTrue(correosEnGrupo.containsAll(correosEsperados) && correosEnGrupo.size() == correosEsperados.size());
    }

    @AfterEach
    public void limpiarBaseDeDatos() {
        EstudianteService estudianteService = new EstudianteService();
        List<String> correos = List.of(correo1, correo2);
        estudianteService.listarEstudiantes().stream()
            .filter(e -> correos.contains(e.correo()))
            .forEach(e -> estudianteService.eliminarEstudiante(e.id()));

        GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
        grupoEstudioService.listarTodos().stream()
            .filter(g -> g.nombre().startsWith("Grupo"))
            .forEach(g -> grupoEstudioService.eliminarGrupo(g.id()));
    }
}