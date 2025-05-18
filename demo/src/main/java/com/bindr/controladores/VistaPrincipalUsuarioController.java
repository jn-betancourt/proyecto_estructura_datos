package com.bindr.controladores;

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
    void handleHoverOff(MouseDragEvent event) {

    }

    @FXML
    void handleHoverOn(MouseDragEvent event) {

    }
}
