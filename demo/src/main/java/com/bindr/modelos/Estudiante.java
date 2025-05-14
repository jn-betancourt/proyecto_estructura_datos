package com.bindr.modelos;

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

// Clase Estudiante
@Entity(name = "Estudiante")
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Nuevo campo
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String correo;
    @Column(nullable = false, length = 100)
    private String contraseña;

    @Transient
    private List<String> intereses;
    @Transient
    private List<ContenidoEducativo> contenidosPublicados;
    @Transient
    private List<Valoracion> valoracionesRealizadas;
    @Transient
    private List<SolicitudAyuda> solicitudesAyuda;
    @Transient
    private List<Estudiante> conexiones;
    @Transient
    private List<GrupoEstudio> gruposEstudio;
    @Transient
    private List<Mensaje> mensajes;


    public Estudiante(){}

    public Estudiante(String nombre, String correo, String contraseña) {
        this.id = null; // Inicialización del ID

        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.intereses = new ArrayList<>();
        this.contenidosPublicados = new ArrayList<>();
        this.valoracionesRealizadas = new ArrayList<>();
        this.solicitudesAyuda = new ArrayList<>();
        this.conexiones = new ArrayList<>();
        this.gruposEstudio = new ArrayList<>();
        this.mensajes = new ArrayList<>();
    }

    // Métodos
    public void publicarContenido(ContenidoEducativo contenido) {
        contenidosPublicados.add(contenido);
    }

    public void valorarContenido(ContenidoEducativo contenido, int puntuacion, String comentario) {
        Valoracion valoracion = new Valoracion(comentario, contenido, puntuacion, comentario, new Date());
        valoracionesRealizadas.add(valoracion);
        contenido.agregarValoracion(valoracion);
    }

    public void solicitarAyuda(String tema, int urgencia) {
        SolicitudAyuda solicitud = new SolicitudAyuda(tema, this, tema, urgencia, new Date());
        solicitudesAyuda.add(solicitud);
    }

    public void participarEnGrupo(GrupoEstudio grupo) {
        gruposEstudio.add(grupo);
        grupo.agregarEstudiante(this);
    }

    public void enviarMensaje(Estudiante destinatario, String contenido) {
        Mensaje mensaje = new Mensaje(contenido, this, destinatario, contenido, new Date());
        mensajes.add(mensaje);
        destinatario.recibirMensaje(mensaje);
    }

    private void recibirMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
    }

    // Getters y Setters
    public List<Estudiante> getConexiones() {
        return conexiones;
    }
    public int getId() {
        return id;
    }

}