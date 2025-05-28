package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.LoginRequestDTO;
import com.bindr.persistencia.HibernateConfig;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.MensajeService;
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
import java.util.List;
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

    private final AutenticacionService authService = new AutenticacionService();
    private final MensajeService mensajeService = new MensajeService();

    @FXML
    private void irAInicioUsuario(ActionEvent event) {
        String correo = textFieldIngresarCorreo.getText().trim();
        String contraseña = textFieldIngresarContraseña.getText().trim();

        if (correo.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }

        try {

            LoginRequestDTO login = new LoginRequestDTO(correo, contraseña);
            EstudianteDTO estudiante = authService.autenticar(login);

            if (estudiante == null) {
                mostrarAlerta("Error", "Credenciales incorrectas");
                textFieldIngresarContraseña.clear();
                return;
            }
            EntornoData.setEstudianteActual(estudiante);
            
            EntornoData.setConversaciones(
                    MensajeService.obtenerConversacionesPorUsuario(EntornoData.getEstudianteActual().id())
            );


            EntornoData.getConversaciones().forEach(conversacion -> {
                System.out.println("Conversación ID: " + conversacion.id());
                System.out.println("Participantes: " + conversacion.participantes());
            });

            //ciclo para verificar las conversaciones
            for(int i = 0; i < EntornoData.getConversaciones().size(); i++) {
                System.out.println(EntornoData.getConversaciones().get(i).participantes());
            }
            System.out.println(EntornoData.getConversaciones().size());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaPrincipalUsuario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error durante el login");
            e.printStackTrace();
        } finally {
            HibernateConfig.closeEntityManager();
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
            mostrarAlerta("Error", "No se pudo cargar la vista de registro");
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