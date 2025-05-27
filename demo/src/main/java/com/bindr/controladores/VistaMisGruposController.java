package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.servicios.GrupoEstudioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class VistaMisGruposController {

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

    private Long usuarioActualId;
    private final GrupoEstudioService grupoService = new GrupoEstudioService();

    @FXML
    public void initialize() {
        cargarUsuarioActual();
        configurarTabla();
        cargarMisGrupos();
    }

    private void cargarUsuarioActual() {
        usuarioActualId = EntornoData.getEstudianteActual().id();
    }


    private void configurarTabla() {
        // Obtener la columna de la tabla (la única columna "Mis grupos")
        TableColumn<GrupoEstudioDTO, String> columnaGrupos = (TableColumn<GrupoEstudioDTO, String>) tablaMisGrupos.getColumns().get(0);

        // Configurar la celda para mostrar el nombre del grupo
        columnaGrupos.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GrupoEstudioDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GrupoEstudioDTO, String> param) {
                return new SimpleStringProperty(param.getValue().nombre());
            }
        });
    }

    private void cargarMisGrupos() {
        // Obtener todos los grupos
        List<GrupoEstudioDTO> todosLosGrupos = grupoService.listarTodos();

        // Filtrar solo los grupos donde el usuario actual está presente
        List<GrupoEstudioDTO> misGrupos = todosLosGrupos.stream()
                .filter(grupo -> {
                    List<EstudianteDTO> estudiantes = grupo.estudiantes() != null ? grupo.estudiantes() : List.of();
                    return estudiantes.stream().anyMatch(e -> e.id().equals(usuarioActualId));
                })
                .collect(Collectors.toList());

        // Cargar los grupos en la tabla
        if (tablaMisGrupos != null) {
            tablaMisGrupos.getItems().setAll(misGrupos);
        }
    }

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
