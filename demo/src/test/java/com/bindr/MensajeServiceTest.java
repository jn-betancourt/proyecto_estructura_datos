package com.bindr;

import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.EstudianteService;
import com.bindr.servicios.MensajeService;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

public class MensajeServiceTest {

    private final String correo1 = "convuser1@gmail.com";
    private final String correo2 = "convuser2@gmail.com";

    @Test
    public void testCrearConversacionEntreDosUsuarios() {
        AutenticacionService authService = new AutenticacionService();
        EstudianteDTO user1 = authService.registrar(new RegistroRequestDTO("User1", correo1, "pass1"));
        EstudianteDTO user2 = authService.registrar(new RegistroRequestDTO("User2", correo2, "pass2"));

        Assertions.assertNotNull(user1);
        Assertions.assertNotNull(user2);

        // Crear conversación privada (no grupo)
        ConversacionDTO conversacion = MensajeService.crearConversacion(Arrays.asList(correo1, correo2), false);
        Assertions.assertNotNull(conversacion, "La conversación no se creó");
        Assertions.assertFalse(conversacion.esGrupo(), "La conversación debe ser privada");
        Assertions.assertEquals(2, conversacion.participantes().size());
        List<String> correos = conversacion.participantes().stream().map(EstudianteDTO::correo).toList();
        Assertions.assertTrue(correos.containsAll(List.of(correo1, correo2)));
    }

    @AfterEach
    public void limpiar() {
        EstudianteService estudianteService = new EstudianteService();
        estudianteService.listarEstudiantes().stream()
            .filter(e -> correo1.equals(e.correo()) || correo2.equals(e.correo()))
            .forEach(e -> estudianteService.eliminarEstudiante(e.id()));
    }
}