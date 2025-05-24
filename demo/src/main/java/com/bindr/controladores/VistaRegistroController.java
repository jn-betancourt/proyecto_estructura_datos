package com.bindr.controladores;

import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.persistencia.HibernateConfig;
import com.bindr.servicios.AutenticacionService;
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

    private final AutenticacionService authService = new AutenticacionService();

    @FXML
    private void registrarse(ActionEvent event) {
        String nombre = textFieldNombreUsuario.getText().trim();
        String correo = textFieldCorreo.getText().trim();
        String contraseña = textFieldContraseña.getText().trim();

        // Validaciones básicas
        if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }

        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mostrarAlerta("Error", "Formato de correo inválido");
            return;
        }

        try {
            // Crear DTO de registro
            RegistroRequestDTO registro = new RegistroRequestDTO(nombre, correo, contraseña);

            // Intentar registrar usando el servicio
            EstudianteDTO estudianteRegistrado = authService.registrar(registro);

            if (estudianteRegistrado != null) {
                mostrarAlerta("Éxito", "Registro completado exitosamente");
                limpiarCampos(); // Limpiamos los campos después de registro exitoso
            } else {
                mostrarAlerta("Error", "El correo electrónico ya está registrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Ocurrió un error durante el registro: " + e.getMessage());
        } finally {
            // Asegurarse de cerrar la conexión
            HibernateConfig.closeEntityManager();
        }
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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        textFieldNombreUsuario.clear();
        textFieldCorreo.clear();
        textFieldContraseña.clear();
    }
}