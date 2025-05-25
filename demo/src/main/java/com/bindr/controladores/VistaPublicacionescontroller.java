package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dto.PublicacionDTO;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.modelos.Publicacion;
import com.bindr.servicios.PublicacionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private Button btnAtras;

    private ObservableList<Publicacion> publicaciones = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarPublicaciones();
    }

    public void cargarPublicaciones() {
        try {
            // Obtener el ID del estudiante actual desde EntornoData
            Long idEstudianteActual = EntornoData.getEstudianteActual().id();

            System.out.println("Cargando publicaciones para el estudiante ID: " + idEstudianteActual);

            // Usar el método obtenerPorIdDeUsuario del Service
            List<PublicacionDTO> publicacionesDTO = PublicacionService.obtenerPorIdDeUsuario(idEstudianteActual);

            // Convertir DTOs a entidades Publicacion
            List<Publicacion> publicacionesBD = publicacionesDTO.stream()
                    .map(dto -> dto.toEntity())
                    .collect(Collectors.toList());

            System.out.println("Número de publicaciones cargadas: " + publicacionesBD.size());

            publicaciones.setAll(publicacionesBD);
            tablaPublicaciones.setItems(publicaciones);

            // Debug
            publicacionesBD.forEach(p -> {
                System.out.println("Título: " + p.getTitulo() +
                        ", Autor: " + p.getPublicador().getNombre() +
                        ", Fecha: " + p.getFecha());
            });

        } catch (Exception e) {
            System.err.println("Error al cargar publicaciones:");
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar las publicaciones");
        }
    }

    private void configurarColumnas() {
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        columnaAutor.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPublicador().getNombre()));

        columnaTema.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getMaterias().stream()
                                .map(MateriaEstudio::name)
                                .collect(Collectors.joining(", "))));

        columnaFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        columnaArchivo.setCellValueFactory(new PropertyValueFactory<>("archivo"));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    //Navegacion
    @FXML
    void irAInicio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaPrincipalUsuario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}