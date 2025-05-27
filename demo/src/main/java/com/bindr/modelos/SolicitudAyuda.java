package com.bindr.modelos;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una solicitud de ayuda académica entre estudiantes.
 * Cada solicitud tiene un remitente (quien pide ayuda), un destinatario (quien acepta ayudar),
 * una materia, un nivel de urgencia, una fecha de creación, un estado y un mensaje opcional.
 */
@Entity
@Table(name = "solicitud_ayuda")
public class SolicitudAyuda {

    /** Identificador único de la solicitud (clave primaria, autoincremental) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Estudiante que solicita la ayuda (relación muchos a uno, no nulo) */
    @ManyToOne
    @JoinColumn(name = "remitente_id", nullable = false)
    private Estudiante remitente;

    /** Estudiante que acepta la solicitud (relación muchos a uno, puede ser nulo hasta que alguien acepte) */
    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Estudiante destinatario;

    /** Materia o tema sobre el que se solicita ayuda (no nulo) */
    @Column(nullable = false)
    private String materia;

    /** Nivel de urgencia de la solicitud (enum, almacenado como String, no nulo) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelUrgencia urgencia;

    /** Fecha y hora de creación de la solicitud (no nulo) */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Estado de la solicitud: pendiente, aceptada, resuelta, cancelada (no nulo) */
    @Column(nullable = false)
    private String estado;

    /** Mensaje opcional con detalles adicionales de la solicitud (máx 500 caracteres) */
    @Column(length = 500)
    private String mensaje;

    /**
     * Constructor vacío requerido por Hibernate/JPA.
     */
    public SolicitudAyuda() {}

    /**
     * Constructor privado utilizado por el builder.
     * 
     * @param id Identificador de la solicitud
     * @param remitente Estudiante que solicita la ayuda
     * @param destinatario Estudiante que acepta la solicitud
     * @param materia Materia o tema de la solicitud
     * @param urgencia Nivel de urgencia
     * @param fecha Fecha de creación
     * @param estado Estado de la solicitud
     * @param mensaje Mensaje adicional
     */
    private SolicitudAyuda(Integer id, Estudiante remitente, Estudiante destinatario, String materia, NivelUrgencia urgencia,
                           LocalDateTime fecha, String estado, String mensaje) {
        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.materia = materia;
        this.urgencia = urgencia;
        this.fecha = fecha;
        this.estado = estado;
        this.mensaje = mensaje;
    }

    // Getters y setters

    /** @return el identificador único de la solicitud */
    public Integer getId() { return id; }
    /** @param id el identificador único de la solicitud */
    public void setId(Integer id) { this.id = id; }

    /** @return el estudiante remitente */
    public Estudiante getRemitente() { return remitente; }
    /** @param remitente el estudiante que solicita la ayuda */
    public void setRemitente(Estudiante remitente) { this.remitente = remitente; }

    /** @return el estudiante destinatario */
    public Estudiante getDestinatario() { return destinatario; }
    /** @param destinatario el estudiante que acepta la solicitud */
    public void setDestinatario(Estudiante destinatario) { this.destinatario = destinatario; }

    /** @return la materia o tema de la solicitud */
    public String getMateria() { return materia; }
    /** @param materia la materia o tema de la solicitud */
    public void setMateria(String materia) { this.materia = materia; }

    /** @return el nivel de urgencia de la solicitud */
    public NivelUrgencia getUrgencia() { return urgencia; }
    /** @param urgencia el nivel de urgencia de la solicitud */
    public void setUrgencia(NivelUrgencia urgencia) { this.urgencia = urgencia; }

    /** @return la fecha de creación de la solicitud */
    public LocalDateTime getFecha() { return fecha; }
    /** @param fecha la fecha de creación de la solicitud */
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    /** @return el estado de la solicitud */
    public String getEstado() { return estado; }
    /** @param estado el estado de la solicitud */
    public void setEstado(String estado) { this.estado = estado; }

    /** @return el mensaje adicional de la solicitud */
    public String getMensaje() { return mensaje; }
    /** @param mensaje el mensaje adicional de la solicitud */
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    /**
     * Builder para construir instancias de SolicitudAyuda de forma flexible y legible.
     */
    public static class Builder {
        private Integer id;
        private Estudiante remitente;
        private Estudiante destinatario;
        private String materia;
        private NivelUrgencia urgencia;
        private LocalDateTime fecha;
        private String estado;
        private String mensaje;

        /** @param id identificador de la solicitud */
        public Builder id(Integer id) { this.id = id; return this; }
        /** @param remitente estudiante que solicita la ayuda */
        public Builder remitente(Estudiante remitente) { this.remitente = remitente; return this; }
        /** @param destinatario estudiante que acepta la solicitud */
        public Builder destinatario(Estudiante destinatario) { this.destinatario = destinatario; return this; }
        /** @param materia materia o tema de la solicitud */
        public Builder materia(String materia) { this.materia = materia; return this; }
        /** @param urgencia nivel de urgencia */
        public Builder urgencia(NivelUrgencia urgencia) { this.urgencia = urgencia; return this; }
        /** @param fecha fecha de creación */
        public Builder fecha(LocalDateTime fecha) { this.fecha = fecha; return this; }
        /** @param estado estado de la solicitud */
        public Builder estado(String estado) { this.estado = estado; return this; }
        /** @param mensaje mensaje adicional */
        public Builder mensaje(String mensaje) { this.mensaje = mensaje; return this; }

        /**
         * Construye una instancia de SolicitudAyuda con los parámetros definidos.
         * @return nueva instancia de SolicitudAyuda
         */
        public SolicitudAyuda build() {
            return new SolicitudAyuda(id, remitente, destinatario, materia, urgencia, fecha, estado, mensaje);
        }
    }
}
