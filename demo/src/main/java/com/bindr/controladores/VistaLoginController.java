package com.bindr.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VistaLoginController {

    @FXML
    private Button btnIniciarSesionLogin;

    @FXML
    private Button btnRegistrarseLogin;

    @FXML
    private PasswordField textFieldIngresarContraseña;

    @FXML
    private TextField textFieldIngresarCorreo;

    private static final Map<String, String> USUARIOS_REGISTRADOS = new HashMap<>();
    static {
        USUARIOS_REGISTRADOS.put("admin@bindr.com", "admin123"); // Usuario por defecto
    }
    // Método para validar credenciales (debe ser static)
    public static boolean validarCredenciales(String usuario, String contraseña) {
        return USUARIOS_REGISTRADOS.containsKey(usuario) &&
                USUARIOS_REGISTRADOS.get(usuario).equals(contraseña);
    }

    // Método para agregar usuarios desde el registro
    public static void agregarUsuarioRegistrado(String usuario, String contraseña) {
        USUARIOS_REGISTRADOS.put(usuario, contraseña);
    }

    @FXML
    private void irAInicioUsuario(ActionEvent event) {
        String correo = textFieldIngresarCorreo.getText().trim();
        String contraseña = textFieldIngresarContraseña.getText().trim();

        // 1. Validar campos vacíos
        if (correo.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }

        // 2. Verificar credenciales con el sistema
        if (!VistaLoginController.validarCredenciales(correo, contraseña)) {
            mostrarAlerta("Error", "Credenciales incorrectas");
            textFieldIngresarContraseña.clear();
            return;
        }

        // 3. Redirigir a vista principal si es válido
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaPrincipalUsuario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la vista principal");
            e.printStackTrace();
        }
    }

    @FXML
    private void irARegistrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaRegistro.fxml"));
            Parent configView = loader.load();

            Scene scene = new Scene(configView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
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
