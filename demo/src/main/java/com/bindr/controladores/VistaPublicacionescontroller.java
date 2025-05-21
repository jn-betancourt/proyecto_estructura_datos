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
import javafx.stage.Stage;

import java.io.IOException;

public class VistaPublicacionescontroller {

    @FXML
    private Button btnAtras;

    @FXML
    private TextField labelArchivo;

    @FXML
    private TextField labelArchivo1;

    @FXML
    private TextField labelArchivo11;

    @FXML
    private TextField labelArchivo111;

    @FXML
    private TextField labelAutor;

    @FXML
    private TextField labelAutor1;

    @FXML
    private TextField labelAutor11;

    @FXML
    private TextField labelAutor111;

    @FXML
    private TextField labelFecha;

    @FXML
    private TextField labelFecha1;

    @FXML
    private TextField labelFecha11;

    @FXML
    private TextField labelFecha111;

    @FXML
    private TextField labelTema;

    @FXML
    private TextField labelTema1;

    @FXML
    private TextField labelTema11;

    @FXML
    private TextField labelTema111;

    @FXML
    private TextField labelTituloContenido;

    @FXML
    private TextField labelTituloContenido1;

    @FXML
    private TextField labelTituloContenido11;

    @FXML
    private TextField labelTituloContenido111;

    @FXML
    private Label nombreUsuario;

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
