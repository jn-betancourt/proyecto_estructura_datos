package com.bindr.modelos;

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private Integer id;
    private String nombre;
    private String correo;
    private String contrasena;
    private List<Conversacion> conversaciones;

    public Estudiante() {
        this.conversaciones = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<Conversacion> getConversaciones() {
        return conversaciones;
    }

    public void setConversaciones(List<Conversacion> conversaciones) {
        this.conversaciones = conversaciones;
    }

    public static class Builder {
        private Integer id;
        private String nombre;
        private String correo;
        private String contrasena;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder correo(String correo) {
            this.correo = correo;
            return this;
        }

        public Builder contrasena(String contrasena) {
            this.contrasena = contrasena;
            return this;
        }

        public Estudiante build() {
            Estudiante estudiante = new Estudiante();
            estudiante.setId(id);
            estudiante.setNombre(nombre);
            estudiante.setCorreo(correo);
            estudiante.setContrasena(contrasena);
            return estudiante;
        }
    }
}
