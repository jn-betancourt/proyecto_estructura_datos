package com.bindr;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.GrupoEstudio;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.modelos.Conversacion;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.EstudianteService;
import com.bindr.servicios.GrupoEstudioService;
import com.bindr.servicios.MensajeService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class GrupoEstudioIntegracionTest {

     // Variables globales para usar en ambos métodos
    private final String nombreGrupo = "Grupo de Prueba";
    private final String correo1 = "correo1@gmail.com";
    private final String correo2 = "correo2@gmail.com";


    @Test
    public void testCrearGrupoEstudioConConversacion() {
        // Crear dos estudiantes
        AutenticacionService estudianteService = new AutenticacionService();
        EstudianteDTO estudiante1 = estudianteService.registrar(new RegistroRequestDTO("usuario1", "correo1@gmail.com", "pass1"));
        EstudianteDTO estudiante2 = estudianteService.registrar(new RegistroRequestDTO("usuario2", "correo2@gmail.com", "pass1"));
        System.out.println(estudiante1);
        // Crear una conversación grupal
        MensajeService conversacionService = new MensajeService();
        List<String> participantes = Arrays.asList(estudiante1.correo(), estudiante2.correo());
        ConversacionDTO conversacion = conversacionService.crearConversacion(participantes, true);

        // Crear grupo de estudio y asociar la conversación
        GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
        GrupoEstudioDTO grupo = new GrupoEstudioDTO(
            null, // ID se generará automáticamente
            "Grupo de Prueba",
            List.of(MateriaEstudio.BIOLOGIA), // Materias no se especifican en este test
            List.of(estudiante1, estudiante2),
            conversacion
        );
        boolean grupoEstudio = grupoEstudioService.crearGrupo(grupo);

        // Verifica que el grupo fue creado exitosamente
        Assertions.assertTrue(grupoEstudio);

        // Recupera el grupo creado (puedes buscar por nombre si no tienes el ID)
        GrupoEstudioDTO grupoGuardado = grupoEstudioService.listarTodos().stream()
            .filter(g -> "Grupo de Prueba".equals(g.nombre()))
            .findFirst()
            .orElse(null);

        Assertions.assertNotNull(grupoGuardado, "El grupo no fue encontrado en la base de datos");
        Assertions.assertEquals("Grupo de Prueba", grupoGuardado.nombre());

        // Verifica que los participantes son los mismos
        List<String> correosEsperados = List.of(estudiante1.correo(), estudiante2.correo());
        List<String> correosEnGrupo = grupoGuardado.estudiantes().stream()
            .map(EstudianteDTO::correo)
            .toList();
        Assertions.assertTrue(correosEnGrupo.containsAll(correosEsperados) && correosEnGrupo.size() == correosEsperados.size());

        // Verifica que la conversación está asociada y es de grupo
        Assertions.assertNotNull(grupoGuardado.conversacion());
        Assertions.assertTrue(grupoGuardado.conversacion().esGrupo());
        Assertions.assertEquals(conversacion.id(), grupoGuardado.conversacion().id());
    }
     @AfterEach
    public void limpiarBaseDeDatos() {
        EstudianteService estudianteService = new EstudianteService();
        EstudianteDTO estudiante1 = estudianteService.buscarPorCorreo(correo1);
        MensajeService.obtenerConversacionesPorUsuario(estudiante1.id()).forEach(conversacion -> {
            MensajeService.eliminarConversacion(conversacion.id());
        });
        GrupoEstudioService grupoEstudioService = new GrupoEstudioService();
        // Elimina el grupo de prueba si existe
        grupoEstudioService.listarTodos().stream()
            .filter(g -> nombreGrupo.equals(g.nombre()))
            .forEach(g -> grupoEstudioService.eliminarGrupo(g.id()));

        // Elimina los estudiantes de prueba si existen
        estudianteService.listarEstudiantes().stream()
            .filter(e -> correo1.equals(e.correo()) || correo2.equals(e.correo()))
            .forEach(e -> estudianteService.eliminarEstudiante(e.id()));
    }
}