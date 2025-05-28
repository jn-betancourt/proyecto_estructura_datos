package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dao.EstudianteDao;
import com.bindr.dto.ConversacionDTO;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.dto.MensajeDTO;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.servicios.EstudianteService;
import com.bindr.servicios.GrupoEstudioService;
import com.bindr.servicios.MensajeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import javax.swing.*;

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
        cargarGruposDisponibles();
    }

    private void cargarUsuarioActual() {
        usuarioActualId = EntornoData.getEstudianteActual().id();
    }

    @FXML
    void crearGrupo(ActionEvent event) {
        ConversacionDTO con = mensajeService.crearConversacion(List.of(EntornoData.getEstudianteActual().correo()), true);
        GrupoEstudioDTO grupoNuevo = new GrupoEstudioDTO(
                null, // ID se asignará automáticamente
                labelTituloContenido.getText(),
                List.of(desplegableMaterias.getValue()), // Materia seleccionada
                new ArrayList<>(List.of(EntornoData.getEstudianteActual())), // AGREGA EL USUARIO QUE ESTA CREANDO EL GRUPO, NO HAY ADMIN DE GRUPO XD
                 con// Conversación inicial
        );
        System.out.println(grupoService.crearGrupo(grupoNuevo));
    }
    // Método auxiliar para limpiar campos (opcional)
    private void limpiarCampos() {
        labelTituloContenido.clear();
        desplegableMaterias.getSelectionModel().clearSelection();
    }

    // Método auxiliar para mostrar alerts
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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