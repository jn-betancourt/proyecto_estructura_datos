package com.bindr;

import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.LoginRequestDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.dto.SolicitudAyudaDTO;
import com.bindr.modelos.NivelUrgencia;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.SolicitudAyudaService;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

public class SolicitudAyudaServiceTest {

    private static final String correoRemitente = "remitente@test.com";
    private static final String correoDestinatario = "destinatario@test.com";
    private static final String nombreRemitente = "Remitente";
    private static final String nombreDestinatario = "Destinatario";
    private static final String password = "testpass";

    private EstudianteDTO remitente;
    private EstudianteDTO destinatario;
    private SolicitudAyudaService service;

    @BeforeEach
    public void setUp() {
        AutenticacionService auth = new AutenticacionService();
        remitente = auth.registrar(new RegistroRequestDTO(nombreRemitente, correoRemitente, password));
        destinatario = auth.registrar(new RegistroRequestDTO(nombreDestinatario, correoDestinatario, password));
        service = new SolicitudAyudaService();
    }

    @Test
    public void testCicloCompletoSolicitudAyuda() {
        // Crear solicitud
        SolicitudAyudaDTO solicitud = new SolicitudAyudaDTO(
                null,
                remitente,
                null,
                "Matemáticas",
                NivelUrgencia.ALTA,
                LocalDateTime.now(),
                "pendiente",
                "Necesito ayuda urgente con integrales"
        );
        boolean creada = service.crearSolicitud(solicitud);
        Assertions.assertTrue(creada);

        // Listar solicitudes por prioridad
        List<SolicitudAyudaDTO> solicitudes = service.listarSolicitudesPorPrioridad();
        Assertions.assertFalse(solicitudes.isEmpty());
        SolicitudAyudaDTO creadaDTO = solicitudes.get(0);
        Assertions.assertEquals("pendiente", creadaDTO.estado());

        // Aceptar solicitud
        SolicitudAyudaDTO aceptada = service.aceptarSolicitud(creadaDTO.id(), destinatario);
        Assertions.assertNotNull(aceptada);
        Assertions.assertEquals("aceptada", aceptada.estado());
        Assertions.assertEquals(destinatario.id(), aceptada.destinatario().id());

        // Resolver solicitud
        SolicitudAyudaDTO resuelta = service.resolverSolicitud(aceptada.id());
        Assertions.assertNotNull(resuelta);
        Assertions.assertEquals("resuelta", resuelta.estado());

        // Cancelar (no debería poder cancelar una resuelta)
        SolicitudAyudaDTO cancelada = service.cancelarSolicitud(resuelta.id());
        Assertions.assertNull(cancelada);
    }

    @AfterEach
    public void limpiar() {
        // Elimina estudiantes de prueba
        SolicitudAyudaService sol = new SolicitudAyudaService();
        sol.listarSolicitudesPorPrioridad().forEach(s -> sol.eliminarSolicitud(s.id()));
        AutenticacionService auth = new AutenticacionService();
        EstudianteDTO rem = auth.autenticar(new LoginRequestDTO(correoRemitente, password));
        EstudianteDTO dest = auth.autenticar(new LoginRequestDTO(correoDestinatario, password));
        if (rem != null) {
            new com.bindr.servicios.EstudianteService().eliminarEstudiante(rem.id());
        }
        if (dest != null) {
            new com.bindr.servicios.EstudianteService().eliminarEstudiante(dest.id());
        }
    }
}
