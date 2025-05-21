package com.bindr.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class VistaRegistroController {

    @FXML private PasswordField textFieldContraseña;
    @FXML private TextField textFieldCorreo;
    @FXML private TextField textFieldNombreUsuario;

    @FXML
    private void registrarse(ActionEvent event) {
        String nombre = textFieldNombreUsuario.getText().trim();
        String correo = textFieldCorreo.getText().trim();
        String contraseña = textFieldContraseña.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }

        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mostrarAlerta("Error", "Formato de correo inválido");
            return;
        }

        VistaLoginController.agregarUsuarioRegistrado(correo, contraseña);
        mostrarAlerta("Éxito", "Registro completado");
        irALogin(event);
    }

    @FXML
    private void irALogin(ActionEvent event) {
        try {
            Stage stage = (Stage) textFieldCorreo.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/VistaLogin.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar el login");
        }
    }

    @FXML
    private void irARegistrar(ActionEvent event) {
        try {
            Stage stage = (Stage) textFieldCorreo.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/VistaLogin.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar el login");
        }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}