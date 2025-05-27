package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dao.EstudianteDao;
import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.servicios.EstudianteService;
import com.bindr.servicios.GrupoEstudioService;
import com.bindr.servicios.PublicacionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

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
    private ComboBox<String> desplegableMaterias;

    @FXML
    private TableView<?> tablaContenidoPublicacionesGrupos;

    @FXML
    private TableView<GrupoEstudioDTO> tablaMisGrupos;

    @FXML
    private TextField textAutor;

    @FXML
    private TextField textTituloContenido;

    private File archivoSeleccionado;



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



    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void subirArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Documentos", "*.docx"),
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg")
        );

        archivoSeleccionado = fileChooser.showOpenDialog(btnSubirArchivo.getScene().getWindow());

        if (archivoSeleccionado != null) {
            mostrarAlerta("Éxito", "Archivo seleccionado: " + archivoSeleccionado.getName());
        }
    }

}
