package com.bindr.servicios;

import com.bindr.dao.EstudianteDao;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.LoginRequestDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.modelos.Estudiante;

public class AutenticacionService {
    public AutenticacionService(){}
    // Método para autenticarse
    public EstudianteDTO autenticar(LoginRequestDTO login) {
        Estudiante estudiante = EstudianteDao.buscarPorEmail(login.correo());

        if (estudiante != null && estudiante.getContraseña().equals(login.contraseña())) {
            return new EstudianteDTO(estudiante.getId(), estudiante.getNombre(), estudiante.getCorreo());
        }

        return null; // O lanzar una excepción personalizada de autenticación fallida
    }

    // Método para registrar un nuevo estudiante
    public EstudianteDTO registrar(RegistroRequestDTO registro) {
        // Verificamos si ya existe un estudiante con ese correo
        if (EstudianteDao.buscarPorEmail(registro.correo()) != null) {
            return null; // Ya existe
        }

        Estudiante nuevo = Estudiante.builder().nombre(registro.nombre()).contrasena(registro.contraseña()).correo(registro.correo()).build();
        System.out.println(nuevo);
        boolean creado = EstudianteDao.crearEstudiante(nuevo);
        System.out.println(creado);
        if (creado) {
            return new EstudianteDTO(nuevo.getId(), nuevo.getNombre(), nuevo.getCorreo());
        }

        return null; // O lanzar excepción de fallo en creación
    }

}
