package com.bindr;

import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.EstudianteService;
import org.junit.jupiter.api.*;

public class EstudianteServiceTest {

    private final String correo = "servtest@gmail.com";
    private final String nombre = "Estudiante Servicio";
    private final String password = "servpass";

    @Test
    public void testBuscarYEliminarEstudiante() {
        AutenticacionService authService = new AutenticacionService();
        EstudianteDTO registrado = authService.registrar(new RegistroRequestDTO(nombre, correo, password));
        Assertions.assertNotNull(registrado);

        EstudianteService estudianteService = new EstudianteService();
        EstudianteDTO buscado = estudianteService.buscarPorCorreo(correo);
        Assertions.assertNotNull(buscado);
        Assertions.assertEquals(nombre, buscado.nombre());

        // Eliminar estudiante
        boolean eliminado = estudianteService.eliminarEstudiante(buscado.id());
        Assertions.assertTrue(eliminado);

        // Verificar que ya no existe
        EstudianteDTO yaNoExiste = estudianteService.buscarPorCorreo(correo);
        Assertions.assertNull(yaNoExiste);
    }
}