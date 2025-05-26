package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.modelos.MateriaEstudio;
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


public class VistaPerfilUsuarioController {

    @FXML private Button btnPublicar;
    @FXML private Button btnSubirArchivo;
    @FXML private TextField labelTipoUsuario;
    @FXML private TextField labelDescripcion;
    @FXML private TextField labelInstitucion;
    @FXML private DatePicker date;
    @FXML private TextField labelTituloContenido;
    @FXML private TextField labelTema;
    @FXML private TextField labelAutor;
    @FXML private Label nombreUsuario;
    @FXML private TextField textFieldDescripcion;
    @FXML private TextField textFieldTipoUsuario;
    @FXML private TextField textFieldInstitucion;

    private File archivoSeleccionado;
    private EstudianteDTO estudianteActual;
    private VistaPublicacionescontroller publicacionesController;

    @FXML
    private ComboBox<String> desplegableMaterias;

    public void initialize() {

        desplegableMaterias.getItems().addAll(
                "BIOLOGIA",
                "MATEMATICAS",
                "LENGUAS",
                "MUSICA",
                "PINTURA",
                "INFORMATICA"
        );
        desplegableMaterias.setOnAction(event -> {
            String seleccion = desplegableMaterias.getValue();
            System.out.println("Materia seleccionada: " + seleccion);
        });
    }

    // Método de inicialización
    public void initData(EstudianteDTO estudiante) {
        this.estudianteActual = EntornoData.getEstudianteActual();
        this.nombreUsuario.setText(estudiante.nombre());
    }

    public void setPublicacionesController(VistaPublicacionescontroller controller) {
        this.publicacionesController = controller;
    }

    @FXML
    void guardar(ActionEvent event) {
        String tipoUsuario = textFieldTipoUsuario.getText();
        String descripcion = textFieldDescripcion.getText();
        String institucion = textFieldInstitucion.getText();

        if(tipoUsuario.isEmpty() || descripcion.isEmpty() || institucion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }

        // Asignar los valores a los labels de visualización
        labelTipoUsuario.setText(tipoUsuario);
        labelDescripcion.setText(descripcion);
        labelInstitucion.setText(institucion);

        mostrarAlerta("Éxito", "Perfil guardado correctamente");
    }


    @FXML
    private void subirArchivo() {
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

    @FXML
    private void publicar() {
        if (!validarCampos()) return;

        try {
            // 1. Crear DTO usando la selección del ComboBox
            PublicacionDTO publicacion = new PublicacionDTO(
                    null, // ID se generará automáticamente
                    EntornoData.getEstudianteActual(), // Usuario actual
                    LocalDateTime.now(), // Fecha actual
                    labelTituloContenido.getText().trim(), // Título del TextField
                    List.of(MateriaEstudio.valueOf(desplegableMaterias.getValue())), // Materia del ComboBox


                    //==============================================================================
                    List.of(), // Valoraciones vacías por ahora ESTO ES LO QUE HAY QUE MODIFICAR 
                    //==============================================================================


                    archivoSeleccionado.getName() // Nombre del archivo
            );

            // 2. Guardar publicación
            PublicacionService service = new PublicacionService();
            InputStream archivoStream = new FileInputStream(archivoSeleccionado);

            if (service.crearPublicacionConArchivo(publicacion, archivoStream, archivoSeleccionado.getName())) {
                mostrarAlerta("Éxito", "Publicación creada correctamente");
                limpiarCampos();

                // 3. Actualizar vista de publicaciones
                if (publicacionesController != null) {
                    publicacionesController.cargarPublicaciones();
                }
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error", "Selecciona una materia válida");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al publicar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Métodos auxiliares
    private boolean validarCampos() {
        if (labelTituloContenido.getText().trim().isEmpty() ||
                desplegableMaterias.getValue() == null || // Ahora validamos el ComboBox
                date.getValue() == null ||
                archivoSeleccionado == null) {

            mostrarAlerta("Error", "Complete todos los campos y seleccione un archivo");
            return false;
        }
        return true;
    }

    private PublicacionDTO crearPublicacionDTO() throws Exception {
        MateriaEstudio materia = MateriaEstudio.valueOf(labelTema.getText().toUpperCase());
        InputStream archivoStream = new FileInputStream(archivoSeleccionado);
        System.out.println(EntornoData.getEstudianteActual());
        return new PublicacionDTO(
                null,
                EntornoData.getEstudianteActual(),
                LocalDateTime.now(),
                labelTituloContenido.getText().trim(),
                List.of(materia),


                //==============================================================================
                    List.of(), // Valoraciones vacías por ahora ESTO ES LO QUE HAY QUE MODIFICAR 
                //==============================================================================

                
                archivoSeleccionado.getName()
        );
    }

    private boolean guardarPublicacion(PublicacionService service, PublicacionDTO dto) throws Exception {
        InputStream archivoStream = new FileInputStream(archivoSeleccionado);
        return service.crearPublicacionConArchivo(dto, archivoStream, archivoSeleccionado.getName());
    }

    private void actualizarVistaPublicaciones() {
        if (publicacionesController != null) {
            publicacionesController.cargarPublicaciones();
        }
    }

    private void limpiarCampos() {
        labelTituloContenido.clear();
        desplegableMaterias.getSelectionModel().clearSelection(); // Limpiar ComboBox
        date.setValue(null);
        archivoSeleccionado = null;
    }

    // Métodos de navegación
    @FXML
    private void irAPublicaciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VistaPublicaciones.fxml"));
            Parent root = loader.load();

            VistaPublicacionescontroller controller = loader.getController();
            controller.cargarPublicaciones(); // Fuerza la carga al abrir la vista

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irAInicio(ActionEvent event) { cargarVista("/VistaPrincipalUsuario.fxml", event); }

    @FXML
    private void cerrarSesion(ActionEvent event) { cargarVista("/VistaLogin.fxml", event); }

    private void cargarVista(String fxml, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
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