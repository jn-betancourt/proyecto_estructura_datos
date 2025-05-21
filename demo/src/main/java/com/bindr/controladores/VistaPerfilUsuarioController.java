package com.bindr.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class VistaPerfilUsuarioController {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnGuardarPerfil;

    @FXML
    private Button btnPublicar;

    @FXML
    private Button btnSubirArchivo;

    @FXML
    private DatePicker date;

    @FXML
    private TextField labelAutor;

    @FXML
    private TextField labelDescripcion;

    @FXML
    private TextField labelInstitucion;

    @FXML
    private TextField labelTema;

    @FXML
    private TextField labelTipoUsuario;

    @FXML
    private TextField labelTituloContenido;

    @FXML
    private Label nombreUsuario;

    @FXML
    private TextField textFieldContraseñaAnterior;

    @FXML
    private TextField textFieldContraseñaAnterior1;

    @FXML
    private TextField textFieldContraseñaAnterior11;


    @FXML
    void guardar(ActionEvent event) {

    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
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
    private void irAInicio(ActionEvent event) {
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

