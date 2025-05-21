package com.bindr.modelos;

import java.util.*;


import java.util.ArrayList;
import java.util.List;

public class GrupoEstudio {
    private Integer id;
    private String nombre;
    private MateriaEstudio materia;
    private Conversacion conversacion;
    private List<Estudiante> estudiantes;

    public GrupoEstudio() {
        this.estudiantes = new ArrayList<>();
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

    public MateriaEstudio getMateria() {
        return materia;
    }

    public void setMateria(MateriaEstudio materia) {
        this.materia = materia;
    }

    public Conversacion getConversacion() {
        return conversacion;
    }

    public void setConversacion(Conversacion conversacion) {
        this.conversacion = conversacion;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public void agregarEstudiante(Estudiante estudiante) {
        if (!estudiantes.contains(estudiante)) {
            estudiantes.add(estudiante);
        }
    }

    public boolean contieneEstudiante(Estudiante estudiante) {
        return estudiantes.contains(estudiante);
    }

    public static class Builder {
        private Integer id;
        private String nombre;
        private MateriaEstudio materia;
        private Conversacion conversacion;
        private List<Estudiante> estudiantes = new ArrayList<>();

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder materia(MateriaEstudio materia) {
            this.materia = materia;
            return this;
        }

        public Builder conversacion(Conversacion conversacion) {
            this.conversacion = conversacion;
            return this;
        }

        public Builder estudiantes(List<Estudiante> estudiantes) {
            this.estudiantes = estudiantes;
            return this;
        }

        public Builder agregarEstudiante(Estudiante estudiante) {
            this.estudiantes.add(estudiante);
            return this;
        }

        public GrupoEstudio build() {
            GrupoEstudio grupo = new GrupoEstudio();
            grupo.setId(id);
            grupo.setNombre(nombre);
            grupo.setMateria(materia);
            grupo.setConversacion(conversacion);
            grupo.setEstudiantes(estudiantes);
            return grupo;
        }
    }
}
