package com.bindr;

import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.EstudianteService;
import org.junit.jupiter.api.*;

public class AutenticacionServiceTest {

    private final String correo = "testuser@gmail.com";
    private final String nombre = "Test User";
    private final String password = "testpass";

    @Test
    public void testRegistroYBusquedaUsuario() {
        AutenticacionService authService = new AutenticacionService();
        EstudianteDTO registrado = authService.registrar(new RegistroRequestDTO(nombre, correo, password));
        Assertions.assertNotNull(registrado, "El usuario no se registró correctamente");
        Assertions.assertEquals(nombre, registrado.nombre());
        Assertions.assertEquals(correo, registrado.correo());

        // Verifica que se puede buscar el usuario por correo
        EstudianteService estudianteService = new EstudianteService();
        EstudianteDTO buscado = estudianteService.buscarPorCorreo(correo);
        Assertions.assertNotNull(buscado, "El usuario no se encontró por correo");
        Assertions.assertEquals(nombre, buscado.nombre());
    }

    @AfterEach
    public void limpiar() {
        EstudianteService estudianteService = new EstudianteService();
        estudianteService.listarEstudiantes().stream()
            .filter(e -> correo.equals(e.correo()))
            .forEach(e -> estudianteService.eliminarEstudiante(e.id()));
    }
}