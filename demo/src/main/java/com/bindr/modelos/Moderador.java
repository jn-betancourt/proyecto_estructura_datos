package com.bindr.modelos;

import java.util.*;


// Clase Moderador
class Moderador {
    private String id;
    private String nombre;
    private String correo;
    private String contraseña;

    public Moderador(String id, String nombre, String correo, String contraseña) {
        this.id=id;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public String getId() {
        return id;
    }

    public void gestionarUsuario(Estudiante estudiante) {
        // Lógica de gestión de usuarios
    }

    public void generarReporte() {
        // Lógica para generar reportes
    }
}