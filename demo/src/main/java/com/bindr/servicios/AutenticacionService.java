package com.bindr.servicios;

import com.bindr.dao.EstudianteDao;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.LoginRequestDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.modelos.Estudiante;

/**
 * Servicio encargado de la autenticación y registro de estudiantes.
 * Proporciona métodos para autenticar usuarios y registrar nuevos estudiantes en el sistema.
 */
public class AutenticacionService {

    /**
     * Constructor vacío.
     */
    public AutenticacionService(){}

    /**
     * Autentica a un estudiante usando su correo y contraseña.
     * Busca el estudiante por correo y compara la contraseña proporcionada.
     *
     * @param login DTO con los datos de inicio de sesión (correo y contraseña)
     * @return EstudianteDTO si la autenticación es exitosa, null si falla
     */
    public EstudianteDTO autenticar(LoginRequestDTO login) {
        Estudiante estudiante = EstudianteDao.buscarPorEmail(login.correo());

        // Verifica si el estudiante existe y la contraseña es correcta
        if (estudiante != null && estudiante.getContraseña().equals(login.contraseña())) {
            return new EstudianteDTO(estudiante.getId(), estudiante.getNombre(), estudiante.getCorreo());
        }

        // Retorna null si la autenticación falla
        return null; // O lanzar una excepción personalizada de autenticación fallida
    }

    /**
     * Registra un nuevo estudiante en el sistema.
     * Verifica que no exista un estudiante con el mismo correo antes de crear uno nuevo.
     *
     * @param registro DTO con los datos de registro (nombre, correo, contraseña)
     * @return EstudianteDTO si el registro es exitoso, null si ya existe el correo o falla la creación
     */
    public EstudianteDTO registrar(RegistroRequestDTO registro) {
        // Verificamos si ya existe un estudiante con ese correo
        if (EstudianteDao.buscarPorEmail(registro.correo()) != null) {
            return null; // Ya existe
        }

        // Construye la entidad Estudiante a partir del DTO de registro
        Estudiante nuevo = Estudiante.builder()
            .nombre(registro.nombre())
            .contrasena(registro.contraseña())
            .correo(registro.correo())
            .build();

        System.out.println(nuevo);
        boolean creado = EstudianteDao.crearEstudiante(nuevo);
        System.out.println(creado);

        // Si se creó correctamente, retorna el DTO correspondiente
        if (creado) {
            return new EstudianteDTO(nuevo.getId(), nuevo.getNombre(), nuevo.getCorreo());
        }

        // Retorna null si falla la creación
        return null; // O lanzar excepción de fallo en creación
    }

}
