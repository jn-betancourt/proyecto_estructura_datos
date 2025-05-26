package com.bindr;

import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.EstudianteService;
import com.bindr.servicios.PublicacionService;
import org.junit.jupiter.api.*;

import java.util.List;

public class PublicacionServiceTest {

    private final String correo = "pubtest@gmail.com";
    private final String nombre = "Publicador";
    private final String password = "pubpass";

    @Test
    public void testCrearYEliminarPublicacion() {
        // Crear usuario publicador
        AutenticacionService authService = new AutenticacionService();
        EstudianteDTO publicador = authService.registrar(new RegistroRequestDTO(nombre, correo, password));
        Assertions.assertNotNull(publicador);

        // Crear publicación
        PublicacionDTO publicacion = new PublicacionDTO(
                null,
                publicador,
                null,
                "Título de prueba",
                List.of(MateriaEstudio.BIOLOGIA),
                null
        );
        boolean creada = PublicacionService.crearPublicacion(publicacion);
        Assertions.assertTrue(creada);

        // Buscar publicación por publicador
        List<PublicacionDTO> publicaciones = PublicacionService.obtenerPorIdDeUsuario(publicador.id());
        Assertions.assertFalse(publicaciones.isEmpty());
        PublicacionDTO encontrada = publicaciones.stream()
                .filter(p -> "Título de prueba".equals(p.titulo()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(encontrada);

        // Eliminar publicación
        boolean eliminada = PublicacionService.eliminar(encontrada.id());
        Assertions.assertTrue(eliminada);

        // Limpiar usuario
        EstudianteService estudianteService = new EstudianteService();
        estudianteService.eliminarEstudiante(publicador.id());
    }
}