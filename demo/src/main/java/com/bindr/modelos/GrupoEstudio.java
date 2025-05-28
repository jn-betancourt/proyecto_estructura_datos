package com.bindr.modelos;

import java.util.*;

import com.bindr.converters.MateriaEnumListConverter;

import jakarta.persistence.*;

@Entity
@Table(name = "grupos_de_estudio")
public class GrupoEstudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Convert(converter = MateriaEnumListConverter.class)
    @Column(columnDefinition = "TEXT") // opcional, si la lista es larga
    private List<MateriaEstudio> materia;

    @OneToOne
    @JoinColumn(name = "conversacion_id")
    private Conversacion conversacion;

    @ManyToMany
    @JoinTable(
        name = "grupo_estudiante",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private List<Estudiante> estudiantes;

    @OneToMany(mappedBy = "grupoEstudio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Publicacion> publicaciones = new ArrayList<>();

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

    public List<MateriaEstudio> getMateria() {
        return materia;
    }

    public void setMateria(List<MateriaEstudio> materia) {
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

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
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
        private List<MateriaEstudio> materia;
        private Conversacion conversacion;
        private List<Estudiante> estudiantes = new ArrayList<>();
        private List<Publicacion> publicaciones = new ArrayList<>();

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder materia(List<MateriaEstudio> materia) {
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

        public Builder publicaciones(List<Publicacion> publicaciones) {
            this.publicaciones = publicaciones;
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
            grupo.setPublicaciones(publicaciones);
            return grupo;
        }
    }
}
