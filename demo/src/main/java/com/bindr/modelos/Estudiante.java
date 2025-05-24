package com.bindr.modelos;

import java.util.*;

import jakarta.persistence.*;


@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "contraseña", nullable = false, length = 64)
    private String contraseña;

    @ManyToMany(mappedBy = "participantes")
    private List<Conversacion> conversaciones;

    public Estudiante() {
        this.conversaciones = new ArrayList<>();
    }

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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contrasena) {
        this.contraseña = contrasena;
    }

    public List<Conversacion> getConversaciones() {
        return conversaciones;
    }

    public void setConversaciones(List<Conversacion> conversaciones) {
        this.conversaciones = conversaciones;
    }

    public static Builder builder(){
        return new Builder();
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

        public Estudiante build() {
            Estudiante estudiante = new Estudiante();
            estudiante.setId(id);
            estudiante.setNombre(nombre);
            estudiante.setCorreo(correo);
            estudiante.setContraseña(contrasena);
            return estudiante;
        }
    }
}
