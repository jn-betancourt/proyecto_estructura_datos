package com.bindr.modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bindr.converters.MateriaEnumListConverter;

import jakarta.persistence.*;

@Entity
@Table(name = "publicacion")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publicador_id")
    private Estudiante publicador;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(name = "archivo_uri", nullable = true)
    private String archivo;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Convert(converter = MateriaEnumListConverter.class)
    @Column(columnDefinition = "TEXT") // opcional, si la lista es larga
    private List<MateriaEstudio> materias = new ArrayList<>();

    public Publicacion() {
        this.materias = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<MateriaEstudio> getMaterias() {
        return materias;
    }

    public void setMaterias(List<MateriaEstudio> materias) {
        this.materias = materias;
    }
    public String getArchivo() {
        return archivo;
    }
    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public void agregarMateria(MateriaEstudio materia) {
        if (!materias.contains(materia)) {
            materias.add(materia);
        }
    }

    public static class Builder {
        private Long id;
        private Estudiante publicador;
        private LocalDateTime fecha;
        private String archivo;
        private String titulo;
        private List<MateriaEstudio> materias = new ArrayList<>();

        public Builder id(Long id) {
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

        private Builder archivo(String archivo) {
            this.archivo = archivo;
            return this;
        }

        public Builder titulo(String titulo) {
            this.titulo = titulo;
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
            publicacion.setArchivo(archivo);
            publicacion.setTitulo(titulo);
            publicacion.setMaterias(materias);
            return publicacion;
        }
    }
}
