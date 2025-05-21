package com.bindr.controladores;

import com.bindr.modelos.Publicacion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class VistaPublicacionescontroller {

    @FXML
    private TableView<Publicacion> tablaPublicaciones;
    @FXML
    private TableColumn<Publicacion, String> columnaTitulo;
    @FXML
    private TableColumn<Publicacion, String> columnaAutor;
    @FXML
    private TableColumn<Publicacion, String> columnaTema;
    @FXML
    private TableColumn<Publicacion, String> columnaFecha;
    @FXML
    private TableColumn<Publicacion, String> columnaArchivo;

    // Modelo de datos

    @FXML
    private Button btnAtras;

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