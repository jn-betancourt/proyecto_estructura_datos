package com.bindr.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VistaGruposController {

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnCrearGrupo;

    @FXML
    private Button btnMisGrupos;

    @FXML
    private Button btnUnirmeAGrupo;

    @FXML
    private ComboBox<?> desplegableMaterias;

    @FXML
    private TextField labelAutor;

    @FXML
    private TextField labelTituloContenido;

    @FXML
    private TableView<?> tablaGruposDisponibles;

    @FXML
    void crearGrupo(ActionEvent event) {

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

    @FXML
    void irAMisGrupos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaMisGrupos.fxml"));
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
    void unirmeAGrupo(ActionEvent event) {

    }

}