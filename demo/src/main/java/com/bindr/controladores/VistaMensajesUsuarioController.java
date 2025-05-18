package com.bindr.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class VistaMensajesUsuarioController {

    @FXML
    private ImageView ImageUser;

    @FXML
    private VBox VboxChats;

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnBuscar;

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
    private TextField textFieldBuscaOIniciaChat;

    @FXML
    private TextField textFieldEscribirMensaje;

    @FXML
    void abrirChat(ActionEvent event) {

    }
    @FXML
    void buscarChats(ActionEvent event) {

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
