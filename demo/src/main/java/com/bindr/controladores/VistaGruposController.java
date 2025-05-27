package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.servicios.GrupoEstudioService;
import com.bindr.servicios.MensajeService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class VistaGruposController {

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnCrearGrupo;

    @FXML
    private Button btnMisGrupos;

    @FXML
    private Button btnUnirmeAGrupo;

    @FXML
    private ComboBox<MateriaEstudio> desplegableMaterias;

    @FXML
    private TextField labelAutor;

    @FXML
    private TextField labelTituloContenido;

    @FXML
    private TableView<GrupoEstudioDTO> tablaGruposDisponibles;

    private Long usuarioActualId;
    private final GrupoEstudioService grupoService = new GrupoEstudioService();
    private final MensajeService mensajeService = new MensajeService();

    @FXML
    public void initialize() {
        // Cargar todas las materias del Enum en el ComboBox
        desplegableMaterias.getItems().addAll(MateriaEstudio.values());

        // Opcional: Configurar cómo se muestra el texto en el ComboBox
        desplegableMaterias.setConverter(new StringConverter<>() {
            @Override
            public String toString(MateriaEstudio materia) {
                return materia != null ? materia.name() : "";
            }

            @Override
            public MateriaEstudio fromString(String string) {
                return string != null ? MateriaEstudio.valueOf(string) : null;
            }
        });

        cargarUsuarioActual();
        configurarTablaGruposDisponibles(); // Agregar esta línea
        cargarGruposDisponibles();
    }

    private void cargarUsuarioActual() {
        usuarioActualId = EntornoData.getEstudianteActual().id();
    }

    @FXML
    void crearGrupo(ActionEvent event) {
        // Validar que se hayan llenado los campos requeridos
        if (labelTituloContenido.getText().isEmpty() || desplegableMaterias.getValue() == null) {
            // Puedes agregar una alerta aquí si quieres
            System.out.println("Por favor complete todos los campos");
            return;
        }

        ConversacionDTO con = mensajeService.crearConversacion(List.of(EntornoData.getEstudianteActual().correo()), true);
        GrupoEstudioDTO grupoNuevo = new GrupoEstudioDTO(
                null, // ID se asignará automáticamente
                labelTituloContenido.getText(),
                List.of(desplegableMaterias.getValue()), // Materia seleccionada
                new ArrayList<>(List.of(EntornoData.getEstudianteActual())), // AGREGA EL USUARIO QUE ESTA CREANDO EL GRUPO
                con // Conversación inicial
        );

        GrupoEstudioDTO grupoCreado = grupoService.crearGrupo(grupoNuevo);
        System.out.println("Grupo creado: " + grupoCreado);

        // Limpiar los campos después de crear el grupo
        labelTituloContenido.clear();
        labelAutor.clear();
        desplegableMaterias.setValue(null);

        // Recargar la tabla para reflejar los cambios
        cargarGruposDisponibles();
    }

    private void cargarGruposDisponibles() {
        List<GrupoEstudioDTO> todosLosGrupos = grupoService.listarTodos();

        List<GrupoEstudioDTO> disponibles = todosLosGrupos.stream()
                .filter(grupo -> {
                    List<EstudianteDTO> estudiantes = grupo.estudiantes() != null ? grupo.estudiantes() : List.of();
                    return estudiantes.stream().noneMatch(e -> e.id().equals(usuarioActualId));
                })
                .toList();

        // Verifica que la tabla no sea null y que tenga tipo correcto
        if (tablaGruposDisponibles != null) {
            tablaGruposDisponibles.getItems().setAll(disponibles);
        }
    }


    private void configurarTablaGruposDisponibles() {
        // Obtener la columna de la tabla (la única columna "Grupos Disponibles")
        TableColumn<GrupoEstudioDTO, String> columnaGrupos = (TableColumn<GrupoEstudioDTO, String>) tablaGruposDisponibles.getColumns().get(0);

        // Configurar la celda para mostrar el nombre del grupo
        columnaGrupos.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GrupoEstudioDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<GrupoEstudioDTO, String> param) {
                return new SimpleStringProperty(param.getValue().nombre());
            }
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

    @FXML
    void irAMisGrupos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaMisGrupos.fxml"));
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
    void unirmeAGrupo(ActionEvent event) {

    }

}