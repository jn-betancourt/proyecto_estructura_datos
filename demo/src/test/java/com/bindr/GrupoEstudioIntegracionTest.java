package com.bindr;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.GrupoEstudio;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.modelos.Conversacion;
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

    @Test
    public void testCrearGrupoEstudioBasico() {
    AutenticacionService estudianteService = new AutenticacionService();
    EstudianteDTO estudiante1 = estudianteService.registrar(new RegistroRequestDTO("usuario1", "correo1@gmail.com", "pass1"));
    EstudianteDTO estudiante2 = estudianteService.registrar(new RegistroRequestDTO("usuario2", "correo2@gmail.com", "pass1"));

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
}

@Test
public void testAsociarConversacionAGrupo() {
    AutenticacionService estudianteService = new AutenticacionService();
    EstudianteDTO estudiante1 = estudianteService.registrar(new RegistroRequestDTO("usuario1", "correo1@gmail.com", "pass1"));
    EstudianteDTO estudiante2 = estudianteService.registrar(new RegistroRequestDTO("usuario2", "correo2@gmail.com", "pass1"));

    MensajeService conversacionService = new MensajeService();
    List<String> participantes = Arrays.asList(estudiante1.correo(), estudiante2.correo());
    ConversacionDTO conversacion = conversacionService.crearConversacion(participantes, true);

    GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
    GrupoEstudioDTO grupo = new GrupoEstudioDTO(
        null,
        "Grupo con Conversacion",
        List.of(MateriaEstudio.BIOLOGIA),
        List.of(estudiante1, estudiante2),
        null,
        List.of()
    );
    GrupoEstudioDTO grupoEstudio = grupoEstudioService.crearGrupo(grupo);

    boolean asociado = grupoEstudioService.asociarConversacion(grupoEstudio.id(), conversacion.id());
    GrupoEstudioDTO grupoActualizado = grupoEstudioService.obtenerPorId(grupoEstudio.id());

    Assertions.assertTrue(asociado, "No se pudo asociar la conversación al grupo");
    Assertions.assertNotNull(grupoActualizado.conversacion());
    Assertions.assertTrue(grupoActualizado.conversacion().esGrupo());
    Assertions.assertEquals(conversacion.id(), grupoActualizado.conversacion().id());
}

@Test
public void testAgregarPublicacionAGrupo() {
    AutenticacionService estudianteService = new AutenticacionService();
    EstudianteDTO estudiante1 = estudianteService.registrar(new RegistroRequestDTO("usuario1", "correo1@gmail.com", "pass1"));

    GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
    GrupoEstudioDTO grupo = new GrupoEstudioDTO(
        null,
        "Grupo con Publicacion",
        List.of(MateriaEstudio.BIOLOGIA),
        List.of(estudiante1),
        null,
        List.of()
    );
    GrupoEstudioDTO grupoEstudio = grupoEstudioService.crearGrupo(grupo);

    // Crear publicación asociada al grupo
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

    // Verificar que la publicación está asociada al grupo
    List<PublicacionDTO> publicaciones = grupoEstudioService.obtenerPublicacionesDeGrupo(grupoEstudio.id());
    Assertions.assertFalse(publicaciones.isEmpty(), "El grupo no tiene publicaciones asociadas");
    Assertions.assertEquals("Título de prueba", publicaciones.get(0).titulo());
}

@Test
public void testParticipantesGrupo() {
    AutenticacionService estudianteService = new AutenticacionService();
    EstudianteDTO estudiante1 = estudianteService.registrar(new RegistroRequestDTO("usuario1", "correo1@gmail.com", "pass1"));
    EstudianteDTO estudiante2 = estudianteService.registrar(new RegistroRequestDTO("usuario2", "correo2@gmail.com", "pass1"));

    GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
    GrupoEstudioDTO grupo = new GrupoEstudioDTO(
        null,
        "Grupo Participantes",
        List.of(MateriaEstudio.BIOLOGIA),
        List.of(estudiante1, estudiante2),
        null,
        List.of()
    );
    GrupoEstudioDTO grupoEstudio = grupoEstudioService.crearGrupo(grupo);

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
    List<String> correos = List.of("correo1@gmail.com", "correo2@gmail.com");
    estudianteService.listarEstudiantes().stream()
        .filter(e -> correos.contains(e.correo()))
        .forEach(e -> estudianteService.eliminarEstudiante(e.id()));

    GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
    grupoEstudioService.listarTodos().stream()
        .filter(g -> g.nombre().startsWith("Grupo"))
        .forEach(g -> grupoEstudioService.eliminarGrupo(g.id()));
}
}