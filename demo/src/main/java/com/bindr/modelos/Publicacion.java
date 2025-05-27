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

    @Transient
    private List<Valoracion> valoraciones = new ArrayList<>();

    @Lob
    @Column(name = "valoraciones_json")
    private String valoracionesJson;

    @ManyToOne
    @JoinColumn(name = "grupo_estudio_id")
    private GrupoEstudio grupoEstudio;

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

    public GrupoEstudio getGrupoEstudio() {
        return grupoEstudio;
    }

    public void setGrupoEstudio(GrupoEstudio grupoEstudio) {
        this.grupoEstudio = grupoEstudio;
    }

    public void agregarMateria(MateriaEstudio materia) {
        if (!materias.contains(materia)) {
            materias.add(materia);
        }
    }

     // Métodos para serializar/deserializar valoraciones
    // public void serializarValoraciones() {
    //     if (valoraciones != null) {
    //         try {
    //             ObjectMapper mapper = new ObjectMapper();
    //             this.valoracionesJson = mapper.writeValueAsString(valoraciones);
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    // public void deserializarValoraciones() {
    //     if (valoracionesJson != null && !valoracionesJson.isBlank()) {
    //         try {
    //             ObjectMapper mapper = new ObjectMapper();
    //             this.valoraciones = mapper.readValue(valoracionesJson, new TypeReference<List<Valoracion>>() {});
    //         } catch (Exception e) {
    //             this.valoraciones = new ArrayList<>();
    //             e.printStackTrace();
    //         }
    //     } else {
    //         this.valoraciones = new ArrayList<>();
    //     }
    // }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
        System.out.println("Valoraciones establecidas: " + valoraciones.size());
        System.out.println(valoracionesJson);
    }

    public String getValoracionesJson() {
        return valoracionesJson;
    }
    public void setValoracionesJson(String valoracionesJson) {
        this.valoracionesJson = valoracionesJson;
    }   

    public static class Builder {
        private Long id;
        private Estudiante publicador;
        private LocalDateTime fecha;
        private String archivo;
        private String titulo;
        private List<Valoracion> valoraciones = new ArrayList<>();
        private List<MateriaEstudio> materias = new ArrayList<>();
        private GrupoEstudio grupoEstudio;

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

        public Builder archivo(String archivo) {
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

        public Builder valoraciones(List<Valoracion> valoraciones) {
            if (valoraciones != null) {
                this.valoraciones = valoraciones;
            }
            return this;

        }

        public Builder grupoEstudio(GrupoEstudio grupoEstudio) {
            this.grupoEstudio = grupoEstudio;
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
            publicacion.setValoraciones(valoraciones);
            publicacion.setGrupoEstudio(grupoEstudio);
            return publicacion;
        }
    }
}
