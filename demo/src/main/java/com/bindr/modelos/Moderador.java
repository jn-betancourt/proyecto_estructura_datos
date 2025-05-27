package com.bindr.modelos;

import jakarta.persistence.*; // O usa javax.persistence.* si tu proyecto lo requiere
import java.util.*;

@Entity
@Table(name = "moderadores")
public class Moderador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contraseña;

    public Moderador() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return contraseña;
    }

    public void setContrasena(String contrasena) {
        this.contraseña = contrasena;
    }

    public static class Builder {
        private Long id;
        private String nombre;
        private String correo;
        private String contrasena;

        public Builder id(Long id) {
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
