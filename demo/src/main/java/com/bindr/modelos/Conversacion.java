package com.bindr.modelos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversacion")
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToMany
    @JoinTable(
        name = "conversacion_estudiante",
        joinColumns = @JoinColumn(name = "conversacion_id"),
        inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private List<Estudiante> participantes;

    @Lob
    @Column(name = "mensajes_json")
    private String mensajesJson;

    @Column(name = "es_grupo", nullable = false)
    private boolean esGrupo;

    @Transient
    private List<Mensaje> mensajes;

    // Constructor privado para uso exclusivo del builder
    public Conversacion() {
        this.participantes = new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<Estudiante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Estudiante> participantes) {
        this.participantes = participantes;
    }

    public boolean isEsGrupo() {
        return esGrupo;
    }

    public void setEsGrupo(boolean esGrupo) {
        this.esGrupo = esGrupo;
    }

    public String getMensajesJson() {
        return mensajesJson;
    }

    public void setMensajesJson(String mensajesJson) {
        this.mensajesJson = mensajesJson;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    // --- Métodos utilitarios ---

    // public void deserializarMensajes() {
    //     if (mensajesJson != null && !mensajesJson.isBlank()) {
    //         try {
    //             ObjectMapper mapper = new ObjectMapper();
    //             this.mensajes = mapper.readValue(mensajesJson, new TypeReference<List<Mensaje>>() {});
    //         } catch (IOException e) {
    //             this.mensajes = new ArrayList<>();
    //             e.printStackTrace();
    //         }
    //     } else {
    //         this.mensajes = new ArrayList<>();
    //     }
    // }

    // public void serializarMensajes() {
    //     if (mensajes != null && !mensajes.isEmpty()) {
    //         try {
    //             ObjectMapper mapper = new ObjectMapper();
    //             this.mensajesJson = mapper.writeValueAsString(mensajes);
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    public void agregarParticipante(Estudiante estudiante) {
        if (!participantes.contains(estudiante)) {
            participantes.add(estudiante);
        }
    }

    public void removerParticipante(Estudiante estudiante) {
        participantes.remove(estudiante);
    }

    // --- Builder interno ---
    public static class Builder {
        private Long id;
        private LocalDateTime fechaCreacion;
        private List<Estudiante> participantes = new ArrayList<>();
        private boolean esGrupo;
        private List<Mensaje> mensajes;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder participantes(List<Estudiante> participantes) {
            this.participantes = participantes;
            return this;
        }

        public Builder esGrupo(boolean esGrupo) {
            this.esGrupo = esGrupo;
            return this;
        }

        public Builder mensajes(List<Mensaje> mensajes) {
            this.mensajes = mensajes;
            return this;
        }

        public Conversacion build() {
            Conversacion conversacion = new Conversacion();
            conversacion.setId(id);
            conversacion.setFechaCreacion(fechaCreacion);
            conversacion.setParticipantes(participantes);
            conversacion.setEsGrupo(esGrupo);
            conversacion.setMensajes(mensajes);

            // Serializa los mensajes a JSON para guardar en DB si es necesario
            // conversacion.serializarMensajes();

            return conversacion;
        }
    }
}
