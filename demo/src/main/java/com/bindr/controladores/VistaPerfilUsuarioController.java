package com.bindr.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
    private TextField textFieldDescripcion;

    @FXML
    private TextField textFieldTipoUsuario;

    @FXML
    private TextField textFieldInstitucion;

    private File archivoSeleccionado; // Ahora usamos directamente el File

    public void setNombreUsuario(String nombre) {
        nombreUsuario.setText(nombre);

    }



    @FXML
    void guardar(ActionEvent event) {
        // 1. Obtener los valores de los campos de entrada
        String tipoUsuario = textFieldTipoUsuario.getText();
        String descripcion = textFieldDescripcion.getText();
        String institucion = textFieldInstitucion.getText();

        // 2. Validar que no estén vacíos (opcional)
        if(tipoUsuario.isEmpty() || descripcion.isEmpty() || institucion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }

        // 3. Asignar los valores a los labels de visualización
        labelTipoUsuario.setText(tipoUsuario);
        labelDescripcion.setText(descripcion);
        labelInstitucion.setText(institucion);

        // 4. Mostrar confirmación
        mostrarAlerta("Éxito", "Perfil guardado correctamente");

        // Aquí podrías agregar lógica para guardar en base de datos
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
    @FXML
    private void irAPublicaciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaPublicaciones.fxml"));
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
    private void subirArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");

        // Configurar filtros para tipos de archivos
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Documentos Word", "*.docx"),
                new FileChooser.ExtensionFilter("Presentaciones", "*.pptx"),
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) btnSubirArchivo.getScene().getWindow();
        archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            mostrarAlerta("Éxito", "Archivo seleccionado: " + archivoSeleccionado.getName());
        }
    }

    @FXML
    private void publicar() {
        // Validación básica
        if (labelTituloContenido.getText().isEmpty() ||
                labelAutor.getText().isEmpty() ||
                labelTema.getText().isEmpty() ||
                date.getValue() == null) {

            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }

        if (archivoSeleccionado == null) {
            mostrarAlerta("Error", "Debes seleccionar un archivo primero");
            return;
        }

        // Aquí iría la lógica para guardar en tu sistema
        String resumen = "Publicación creada:\n" +
                "Título: " + labelTituloContenido.getText() + "\n" +
                "Autor: " + labelAutor.getText() + "\n" +
                "Tema: " + labelTema.getText() + "\n" +
                "Fecha: " + date.getValue() + "\n" +
                "Archivo: " + archivoSeleccionado.getName();

        mostrarAlerta("Publicación Exitosa", resumen);
        limpiarCampos();
    }

    private void limpiarCampos() {
        labelTituloContenido.clear();
        labelAutor.clear();
        labelTema.clear();
        date.setValue(null);
        archivoSeleccionado = null;
    }


}

