package com.bindr.modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Publicacion {
    private Integer id;
    private Estudiante publicador;
    private LocalDateTime fecha;
    private String titulo;
    private String cuerpo;
    private List<MateriaEstudio> materias;

    public Publicacion() {
        this.materias = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Estudiante getPublicador() {
        return publicador;
    }

    public void setPublicador(Estudiante publicador) {
        this.publicador = publicador;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public List<MateriaEstudio> getMaterias() {
        return materias;
    }

    public void setMaterias(List<MateriaEstudio> materias) {
        this.materias = materias;
    }

    public void agregarMateria(MateriaEstudio materia) {
        if (!materias.contains(materia)) {
            materias.add(materia);
        }
    }

    public static class Builder {
        private Integer id;
        private Estudiante publicador;
        private LocalDateTime fecha;
        private String titulo;
        private String cuerpo;
        private List<MateriaEstudio> materias = new ArrayList<>();

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder publicador(Estudiante publicador) {
            this.publicador = publicador;
            return this;
        }

        public Builder fecha(LocalDateTime fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder cuerpo(String cuerpo) {
            this.cuerpo = cuerpo;
            return this;
        }

        public Builder materias(List<MateriaEstudio> materias) {
            this.materias = materias;
            return this;
        }

        public Publicacion build() {
            Publicacion publicacion = new Publicacion();
            publicacion.setId(id);
            publicacion.setPublicador(publicador);
            publicacion.setFecha(fecha);
            publicacion.setTitulo(titulo);
            publicacion.setCuerpo(cuerpo);
            publicacion.setMaterias(materias);
            return publicacion;
        }
    }
}
