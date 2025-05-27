package com.bindr;

import com.bindr.modelos.Estudiante;
import com.bindr.servicios.AfinidadService;
import com.bindr.utils.GrafoAfinidadEstudiantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AfinidadServiceTest {

    private AfinidadService afinidadService;
    private Estudiante estudianteA;
    private Estudiante estudianteB;
    private Estudiante estudianteC;

    @BeforeEach
    void setUp() {
        afinidadService = new AfinidadService();
        estudianteA = Estudiante.builder().nombre("Ana").correo("ana@correo.com").build();
        estudianteB = Estudiante.builder().nombre("Beto").correo("beto@correo.com").build();
        estudianteC = Estudiante.builder().nombre("Caro").correo("caro@correo.com").build();
    }

    @Test
    void testRegistrarInteraccionCreaRelacion() {
        afinidadService.registrarInteraccion(estudianteA, estudianteB);
        Map<Estudiante, Integer> adyacentesA = afinidadService.obtenerAfinidades(estudianteA);
        Map<Estudiante, Integer> adyacentesB = afinidadService.obtenerAfinidades(estudianteB);

        assertTrue(adyacentesA.containsKey(estudianteB));
        assertTrue(adyacentesB.containsKey(estudianteA));
        assertEquals(1, adyacentesA.get(estudianteB));
        assertEquals(1, adyacentesB.get(estudianteA));
    }

    @Test
    void testRegistrarInteraccionIncrementaPeso() {
        afinidadService.registrarInteraccion(estudianteA, estudianteB);
        afinidadService.registrarInteraccion(estudianteA, estudianteB);

        Map<Estudiante, Integer> adyacentesA = afinidadService.obtenerAfinidades(estudianteA);
        assertEquals(2, adyacentesA.get(estudianteB));
    }

    @Test
    void testEstanConectados() {
        afinidadService.registrarInteraccion(estudianteA, estudianteC);
        assertTrue(afinidadService.estanConectados(estudianteA, estudianteC));
        assertFalse(afinidadService.estanConectados(estudianteA, estudianteB));
    }

    @Test
    void testObtenerTodosEstudiantes() {
        afinidadService.registrarInteraccion(estudianteA, estudianteB);
        afinidadService.registrarInteraccion(estudianteA, estudianteC);
        assertEquals(3, afinidadService.obtenerTodosEstudiantes().size());
    }
}