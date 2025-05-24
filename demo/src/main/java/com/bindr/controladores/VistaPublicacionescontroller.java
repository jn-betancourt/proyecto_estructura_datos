package com.bindr.controladores;

import com.bindr.modelos.Estudiante;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.modelos.Publicacion;
import javafx.beans.property.SimpleObjectProperty;
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

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
    private TableColumn<Publicacion,String> columnaFecha;
    @FXML
    private TableColumn<Publicacion,String> columnaArchivo;

    // Modelo de datos

    @FXML
    private Button btnAtras;

    @FXML
    private Label nombreUsuario;

    private ObservableList<Publicacion> publicaciones = FXCollections.observableArrayList();

    public void agregarPublicacion(Publicacion nuevaPublicacion) {
        publicaciones.add(nuevaPublicacion); // Actualiza la lista observable
        tablaPublicaciones.refresh(); // Opcional: fuerza refrescar la tabla (no siempre necesario)
    }

    public ObservableList<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(ObservableList<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
        tablaPublicaciones.setItems(publicaciones);
    }

    @FXML
    public void initialize() {
        columnaTitulo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTitulo()));

        columnaAutor.setCellValueFactory(data -> {
            Estudiante est = data.getValue().getPublicador();
            return new SimpleStringProperty(est != null ? est.toString() : "Sin autor");
        });

        columnaFecha.setCellValueFactory(data -> {
            LocalDate fecha = data.getValue().getFecha();
            return new SimpleStringProperty(fecha != null ? fecha.toString() : "");
        });

        columnaArchivo.setCellValueFactory(data -> {
            File archivo = data.getValue().getArchivo();
            return new SimpleStringProperty(archivo != null ? archivo.getName() : "Sin archivo");
        });

        columnaTema.setCellValueFactory(data -> {
            List<MateriaEstudio> materias = data.getValue().getMaterias();
            // Suponiendo que solo quieres mostrar la primera materia
            String texto = (materias != null && !materias.isEmpty())
                    ? materias.get(0).name() // o .toString()
                    : "Sin materia";
            return new SimpleStringProperty(texto);
        });
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