package com.bindr.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class VistaConfiguracionController {

    @FXML
    private Button btnActualizarFoto;

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnCambiarContraseña;

    @FXML
    private Button btnCambiarCorreo;

    @FXML
    private Button btnCambiarNombre;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private TextField textFieldCambiarNombre;

    @FXML
    private TextField textFieldContraseñaAnterior;

    @FXML
    private TextField textFieldNuevaContraseña;

    @FXML
    private TextField textFieldNuevoCorreo;

    @FXML
    void ActualizarFotoPerfil(ActionEvent event) {

    }

    @FXML
    void cambiarContraseña(ActionEvent event) {

    }

    @FXML
    void cambiarCorreo(ActionEvent event) {

    }

    @FXML
    void cambiarNombreUsuario(ActionEvent event) {

    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaLogin.fxml"));
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

