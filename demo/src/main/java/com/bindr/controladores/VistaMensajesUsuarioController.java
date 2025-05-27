package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.MensajeDTO;
import com.bindr.persistencia.HibernateConfig;
import com.bindr.servicios.EstudianteService;
import com.bindr.servicios.MensajeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class VistaMensajesUsuarioController {

    @FXML
    private VBox VboxChats;

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnIniciarChat;

    @FXML
    private Button btnEnviarMensaje;

    @FXML
    private Button btnUsuario1;

    @FXML
    private Button btnUsuario2;

    @FXML
    private Button btnUsuario3;

    @FXML
    private Button btnUsuario4;

    @FXML
    private Button btnUsuario5;

    @FXML
    private Button btnUsuario6;

    @FXML
    private Button btnUsuario7;

    @FXML
    private Button btnResultadoBusqueda;

    @FXML
    private VBox vboxMensajes;

    @FXML
    private TextField textFieldBuscaOIniciaChat;

    @FXML
    private TextField textFieldEscribirMensaje;

    private Long usuarioActualId;
    private String usuarioActualEmail;
    private ConversacionDTO conversacionActiva;
    private EstudianteService estudianteService = new EstudianteService();

    @FXML
    public void initialize() {
        cargarUsuarioActual(); // Obtiene ID y email de la base de datos
        configurarBotones();
        configurarEventos();
        cargarConversaciones();
    }

    private void cargarUsuarioActual() {
        usuarioActualEmail = EntornoData.getEstudianteActual().correo();
        usuarioActualId = EntornoData.getEstudianteActual().id();
    }

    private void configurarBotones() {
        List<Button> botones = Arrays.asList(btnUsuario1, btnUsuario2, btnUsuario3, btnUsuario4, btnUsuario5, btnUsuario6, btnUsuario7);
        botones.forEach(btn -> {
            btn.setVisible(false);
            btn.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 10;");
        });
    }

    private void cargarConversaciones() {
        List<ConversacionDTO> conversaciones = EntornoData.getConversaciones();

        List<Button> botones = Arrays.asList(btnUsuario1, btnUsuario2, btnUsuario3, btnUsuario4, btnUsuario5, btnUsuario6, btnUsuario7);

        for (int i = 0; i < Math.min(conversaciones.size(), botones.size()); i++) {
            ConversacionDTO conversacion = conversaciones.get(i);
            Button boton = botones.get(i);

            String nombreChat = obtenerNombreChat(conversacion);
            System.out.println(nombreChat);
            boton.setText(nombreChat);
            boton.setVisible(true);
            boton.setUserData(conversacion);

            boton.setOnAction(e -> {
                conversacionActiva = conversacion;
                mostrarMensajes(conversacion);
                resaltarBotonSeleccionado(boton);
            });
        }
    }


    private String obtenerNombreChat(ConversacionDTO conversacion) {

       // if (conversacion == null || conversacion.participantes() == null)
         //   return "Chat desconocido";
        //}

        if (conversacion.esGrupo()) {
            return "Grupo: " + conversacion.participantes().stream()
                    .map(EstudianteDTO::nombre)
                    .collect(Collectors.joining(", "));
        }

        // Para chat individual, obtener el nombre del otro participante
        if (!conversacion.esGrupo())
        for (EstudianteDTO participante : conversacion.participantes()) {
            System.out.println(participante.nombre());
            if (!participante.id().equals(usuarioActualId)) {
                return participante.nombre();
            }
        }

        return "Chat desconocido";
    }


    private void resaltarBotonSeleccionado(Button botonSeleccionado) {
        Arrays.asList(btnUsuario1, btnUsuario2, btnUsuario3, btnUsuario4, btnUsuario5, btnUsuario6, btnUsuario7)
                .forEach(boton -> {
                    boolean seleccionado = boton == botonSeleccionado;
                    String estilo = seleccionado
                            ? "-fx-background-color: #bbdefb; -fx-font-weight: bold;"
                            : "-fx-background-color: #f5f5f5;";
                    boton.setStyle(estilo);
                });
    }

    private void mostrarMensajes(ConversacionDTO conversacion) {
        vboxMensajes.getChildren().clear();

        for (MensajeDTO mensaje : conversacion.mensajes()) {
            boolean esMio = mensaje.autor() != null && mensaje.autor().id().equals(usuarioActualId);

            HBox contenedorMensaje = new HBox(10);
            contenedorMensaje.setAlignment(esMio ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            Label lblMensaje = new Label(mensaje.contenido());
            lblMensaje.setStyle(esMio
                    ? "-fx-background-color: #dcf8c6; -fx-background-radius: 15; -fx-padding: 8 12 8 12;"
                    : "-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-padding: 8 12 8 12;");
            lblMensaje.setWrapText(true);
            lblMensaje.setMaxWidth(300);

            contenedorMensaje.getChildren().add(lblMensaje);
            vboxMensajes.getChildren().add(contenedorMensaje);
        }
    }

    private void configurarEventos() {
        btnEnviarMensaje.setOnAction(e -> enviarMensaje());
        textFieldEscribirMensaje.setOnAction(e -> enviarMensaje());
    }

    @FXML
    private void enviarMensaje() {
        // Validación básica
        if (conversacionActiva == null || textFieldEscribirMensaje.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "No hay conversación seleccionada o el mensaje está vacío");
            return;
        }

        String contenido = textFieldEscribirMensaje.getText().trim();

        try {
            // Enviar mensaje usando el servicio, ahora con EstudianteDTO como autor
            boolean enviado = MensajeService.enviarMensaje(
                    conversacionActiva.id(),
                    EntornoData.getEstudianteActual(), // <-- Cambiado aquí
                    contenido
            );

            if (enviado) {
                // Limpiar campo y actualizar la UI
                Platform.runLater(() -> {
                    textFieldEscribirMensaje.clear();
                    conversacionActiva = MensajeService.obtenerConversacionDTO(conversacionActiva.id());
                    mostrarMensajes(conversacionActiva);
                });
            } else {
                mostrarAlerta("Error", "No se pudo enviar el mensaje");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Excepción al enviar mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void buscar() {
        String correo = textFieldBuscaOIniciaChat.getText().trim();
        System.out.println("buscar");
        if (correo.isEmpty()) {
            btnResultadoBusqueda.setText("Ingrese un correo válido.");
            btnIniciarChat.setDisable(true);
            return;
        }

        System.out.println(correo);
        EstudianteDTO estudiante = estudianteService.buscarPorCorreo(correo);
        System.out.println(estudiante.nombre());
        if (estudiante != null) {
            btnResultadoBusqueda.setText(estudiante.nombre());
            btnIniciarChat.setDisable(false); // Habilita iniciar chat si hay resultado
        } else {
            btnResultadoBusqueda.setText("Usuario no encontrado.");
            btnIniciarChat.setDisable(true);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void abrirChat() {
        iniciarChat(null);
    }


    @FXML
    void iniciarChat(ActionEvent event) {
        String correoBuscado = textFieldBuscaOIniciaChat.getText().trim();
        if (correoBuscado.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar un correo para iniciar el chat.");
            return;
        }

        if (correoBuscado.equalsIgnoreCase(usuarioActualEmail)) {
            mostrarAlerta("Error", "No puedes iniciar un chat contigo mismo.");
            return;
        }

        EstudianteDTO destino = estudianteService.buscarPorCorreo(correoBuscado);
        if (destino == null) {
            mostrarAlerta("Error", "Usuario no encontrado.");
            return;
        }

        // Buscar si ya existe una conversación privada con ese usuario
        List<ConversacionDTO> conversaciones = MensajeService.obtenerConversacionesPorUsuario(usuarioActualId);
        ConversacionDTO existente = null;

        for (ConversacionDTO conv : conversaciones) {
            if (!conv.esGrupo() && conv.participantes().size() == 2) {
                boolean contieneAmbos = conv.participantes().stream()
                        .map(EstudianteDTO::id)
                        .collect(Collectors.toSet())
                        .containsAll(List.of(usuarioActualId, destino.id()));

                if (contieneAmbos) {
                    existente = conv;
                    break;
                }
            }
        }

        if (existente != null) {
            conversacionActiva = existente;
            mostrarMensajes(conversacionActiva);
            resaltarConversacionEnBotones(existente);
        } else {
            // Crear nueva conversación
            ConversacionDTO nueva = MensajeService.crearConversacion(
                    List.of(usuarioActualEmail, destino.correo()), false);

            if (nueva != null) {
                conversacionActiva = nueva;
                mostrarMensajes(conversacionActiva);
                agregarConversacionABotones(nueva);
            } else {
                mostrarAlerta("Error", "No se pudo crear la conversación.");
            }
        }
    }

    private void agregarConversacionABotones(ConversacionDTO conversacion) {
        List<Button> botones = Arrays.asList(btnUsuario1, btnUsuario2, btnUsuario3, btnUsuario4, btnUsuario5, btnUsuario6, btnUsuario7);

        for (Button btn : botones) {
            if (!btn.isVisible()) {
                btn.setText(obtenerNombreChat(conversacion));
                btn.setUserData(conversacion);
                btn.setVisible(true);

                btn.setOnAction(e -> {
                    conversacionActiva = conversacion;
                    mostrarMensajes(conversacion);
                    resaltarBotonSeleccionado(btn);
                });
                break;
            }
        }
    }

    private void resaltarConversacionEnBotones(ConversacionDTO conversacion) {
        List<Button> botones = Arrays.asList(btnUsuario1, btnUsuario2, btnUsuario3, btnUsuario4, btnUsuario5, btnUsuario6, btnUsuario7);

        for (Button btn : botones) {
            Object data = btn.getUserData();
            if (data instanceof ConversacionDTO c && c.id().equals(conversacion.id())) {
                conversacionActiva = c;
                mostrarMensajes(c);
                resaltarBotonSeleccionado(btn);
                return;
            }
        }
    }
    //navegacion
    @FXML
    void irAInicio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaPrincipalUsuario.fxml"));
            Parent configView = loader.load();

            Scene scene = new Scene(configView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
