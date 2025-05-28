package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dto.PublicacionDTO;
import com.bindr.dto.ValoracionDTO;
import com.bindr.servicios.PublicacionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class VistaPrincipalUsuarioController {

    @FXML
    private VBox ContenedorBotonesIzquierda;

    @FXML
    private VBox ContenedorPublicaciones;

    @FXML
    private Button btnAmigos;

    @FXML
    private Button btnAyuda;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnConfiguracion;

    @FXML
    private Button btnGruposDeEstudio;

    @FXML
    private Button btnInicio;

    @FXML
    private Button btnMensajes;

    @FXML
    private Button btnPerfil;

    @FXML
    private Button btnSolicitudes;

    @FXML
    private Button btnSugerencias;

    @FXML
    private Label labelAutor;

    @FXML
    private Label labelDescripcion;

    @FXML
    private Label labelFecha;

    @FXML
    private Label labelTitulo;

    @FXML
    private TextField textFieldBuscar;

    @FXML
    void Click(ActionEvent event) {

    }

    @FXML
    void irAPerfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaPerfilUsuario.fxml"));
            Parent configView = loader.load();

            Scene scene = new Scene(configView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irAMensajeUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaMensajesUsuario.fxml"));
            Parent configView = loader.load();

            Scene scene = new Scene(configView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irAAmigos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaAmigosUsuario.fxml"));
            Parent configView = loader.load();

            Scene scene = new Scene(configView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irAGrupos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaGruposUsuario.fxml"));
            Parent configView = loader.load();

            Scene scene = new Scene(configView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void irAConfiguracion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaConfiguracionUsuario.fxml"));
            Parent configView = loader.load();

            Scene scene = new Scene(configView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleHoverOff(MouseDragEvent event) {

    }

    @FXML
    void handleHoverOn(MouseDragEvent event) {

    }

    @FXML
    public void initialize() {
        cargarPublicaciones();
    }

    private void cargarPublicaciones() {
        // ContenedorPublicaciones.getChildren().clear();
        List<PublicacionDTO> publicaciones = PublicacionService.obtenerTodas();

        for (PublicacionDTO pub : publicaciones) {
            VBox card = crearCardPublicacion(pub);
            ContenedorPublicaciones.getChildren().add(card);
        }
    }

    private VBox crearCardPublicacion(PublicacionDTO pub) {
        VBox card = new VBox();
        card.getStyleClass().add("card-mini");
        card.setSpacing(5);

        Label titulo = new Label(pub.titulo());
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label autor = new Label("Autor: " + (pub.publicador() != null ? pub.publicador().nombre() : "Desconocido"));
        Label fecha = new Label(pub.fecha() != null ? pub.fecha().toString() : "");
        Label materias = new Label("Materias: " + (pub.materias() != null ? pub.materias().toString() : ""));
        Label archivo = new Label(pub.archivo() != null ? "Archivo: " + pub.archivo() : "");
        
        Button btnLike = new Button("👍 Like");

        // Verificar si el usuario actual ya dio like
        boolean yaDioLike = pub.valoraciones().stream()
            .anyMatch(v -> v.autor().equals(EntornoData.getEstudianteActual().correo()) && v.like());

        if (yaDioLike) {
            btnLike.setDisable(true);
            btnLike.setText("Ya te gusta");
        }

        btnLike.setOnAction(e -> {
            // 1. Crear DTO de valoración
            ValoracionDTO valoracion = new ValoracionDTO(
                null,
                EntornoData.getEstudianteActual().correo(), // Reemplaza por el correo del usuario actual
                true,
                java.time.LocalDate.now()
            );

            // 2. Agregar la valoración a la lista de la publicación
            List<ValoracionDTO> nuevasValoraciones = new java.util.ArrayList<>(pub.valoraciones());
            nuevasValoraciones.add(valoracion);

            // 3. Crear un nuevo DTO de publicación actualizado
            PublicacionDTO actualizada = new PublicacionDTO(
                pub.id(),
                pub.publicador(),
                pub.fecha(),
                pub.titulo(),
                pub.materias(),
                nuevasValoraciones,
                pub.archivo()
            );

            // 4. Llamar al servicio para actualizar la publicación
            PublicacionService.actualizarPublicacion(actualizada);

            // 5. (Opcional) Actualizar la vista o mostrar feedback
            btnLike.setDisable(true);
            btnLike.setText("¡Gracias por tu like!");
        });

        card.getChildren().addAll(titulo, autor, fecha, materias, archivo, btnLike);

        return card;
    }
}
