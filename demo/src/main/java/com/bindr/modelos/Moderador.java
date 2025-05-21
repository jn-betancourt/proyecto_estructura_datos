package com.bindr.modelos;

import java.util.*;


public class Moderador {
    private Integer id;
    private String nombre;
    private String correo;
    private String contrasena;

    public Moderador() {}

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

        public Moderador build() {
            Moderador moderador = new Moderador();
            moderador.setId(id);
            moderador.setNombre(nombre);
            moderador.setCorreo(correo);
            moderador.setContrasena(contrasena);
            return moderador;
        }
    }
}
