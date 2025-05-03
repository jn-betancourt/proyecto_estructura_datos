package com.bindr.modelos;

import java.util.*;

// Clase Estudiante
public class Estudiante {
    private String id; // Nuevo campo

    private String nombre;
    private String correo;
    private String contraseña;
    private List<String> intereses;
    private List<ContenidoEducativo> contenidosPublicados;
    private List<Valoracion> valoracionesRealizadas;
    private List<SolicitudAyuda> solicitudesAyuda;
    private List<Estudiante> conexiones;
    private List<GrupoEstudio> gruposEstudio;
    private List<Mensaje> mensajes;

    public Estudiante(String id, String nombre, String correo, String contraseña) {
        this.id = id; // Inicialización del ID

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
    public String getId() {
        return id;
    }

}