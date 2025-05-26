package com.bindr;

import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.dto.ValoracionDTO;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.modelos.Valoracion;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.EstudianteService;
import com.bindr.servicios.PublicacionService;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ValoracionServiceTest {
private final String correo = "valtest@gmail.com";
    private final String nombre = "Valorador";
    private final String password = "valpass";
    private Long publicacionId;
    private Long estudianteId;

    @Test
    public void testAgregarValoracionAPublicacion() {
        // Crear usuario y publicación
        AutenticacionService authService = new AutenticacionService();
        EstudianteDTO publicador = authService.registrar(new RegistroRequestDTO(nombre, correo, password));
        Assertions.assertNotNull(publicador);
        estudianteId = publicador.id();

        PublicacionDTO publicacion = new PublicacionDTO(
                null,
                publicador,
                null,
                "Publicación para valorar",
                List.of(MateriaEstudio.BIOLOGIA),
                List.of(),
                null
        );
        boolean creada = PublicacionService.crearPublicacion(publicacion);
        Assertions.assertTrue(creada);

        // Buscar publicación creada
        List<PublicacionDTO> publicaciones = PublicacionService.obtenerPorIdDeUsuario(publicador.id());
        PublicacionDTO publicada = publicaciones.stream()
                .filter(p -> "Publicación para valorar".equals(p.titulo()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(publicada);
        publicacionId = publicada.id();

        // Crear valoración y agregarla
        ValoracionDTO valoracion = new ValoracionDTO(
                null,
                publicador.correo(),
                true, // Simulando un "like"
                LocalDate.now()
        );


        publicada.valoraciones().add(valoracion);

        PublicacionDTO actualizada = new PublicacionDTO(
                publicada.id(),
                publicada.publicador(),
                publicada.fecha(),
                publicada.titulo(),
                publicada.materias(),
                publicada.valoraciones(),
                publicada.archivo()
        );

        boolean actualizadaOk = PublicacionService.actualizarPublicacion(actualizada);
        Assertions.assertTrue(actualizadaOk);

        // Recuperar y verificar la valoración
        PublicacionDTO verificada = PublicacionService.obtenerPorIdDeUsuario(publicador.id()).stream()
                .filter(p -> p.id().equals(actualizada.id()))
                .findFirst()
                .orElse(null);
        System.out.println(verificada.titulo() + " tiene " + verificada.valoraciones().size() + " valoraciones.");
        Assertions.assertNotNull(verificada);
        Assertions.assertFalse(verificada.valoraciones().isEmpty());
    }

    @AfterEach
    public void limpiarBaseDeDatos() {
        if (publicacionId != null) {
            PublicacionService.eliminar(publicacionId);
        }
        if (estudianteId != null) {
            EstudianteService estudianteService = new EstudianteService();
            estudianteService.eliminarEstudiante(estudianteId);
        }
    }
}