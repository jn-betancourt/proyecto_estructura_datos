package com.bindr.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class VistaMisGrupos {

    @FXML
    private Button btnAbandonarGrupo;

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnPublicarEnGrupo;

    @FXML
    private Button btnSubirArchivo;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<?> desplegableMaterias;

    @FXML
    private TableView<?> tablaContenidoPublicacionesGrupos;

    @FXML
    private TableView<?> tablaMisGrupos;

    @FXML
    private TextField textAutor;

    @FXML
    private TextField textTituloContenido;

    @FXML
    void irAInicio(ActionEvent event) {
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
    void publicar(ActionEvent event) {

    }

    @FXML
    void subirArchivo(ActionEvent event) {

    }

}
