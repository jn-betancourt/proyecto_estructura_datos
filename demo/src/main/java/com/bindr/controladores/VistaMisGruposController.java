package com.bindr.controladores;

import com.bindr.EntornoData;
import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.GrupoEstudioDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.GrupoEstudio;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.modelos.Publicacion;
import com.bindr.servicios.GrupoEstudioService;
import com.bindr.servicios.PublicacionService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private ComboBox<MateriaEstudio> desplegableMaterias;

    @FXML
    private ComboBox<GrupoEstudioDTO> desplegableGrupos;

    @FXML
    private TableView<PublicacionDTO> tablaContenidoPublicacionesGrupos;

    private TableColumn<PublicacionDTO, String> columnaTitulo;
    private TableColumn<PublicacionDTO, String> columnaAutor;
    private TableColumn<PublicacionDTO, String> columnaMateria;
    private TableColumn<PublicacionDTO, LocalDateTime> columnaFecha;
    private TableColumn<PublicacionDTO, String> columnaArchivo;

    @FXML
    private TableView<GrupoEstudioDTO> tablaMisGrupos;

    @FXML
    private TextField textAutor;

    @FXML
    private TextField textTituloContenido;

    private File archivoSeleccionado;
    private Long usuarioActualId;
    private final GrupoEstudioService grupoService = new GrupoEstudioService();
    private final PublicacionService publicacionService = new PublicacionService();
    private List<GrupoEstudioDTO> misGrupos;


    @FXML
    public void initialize() {
        cargarUsuarioActual();
        configurarTabla();
        configurarTablaPublicaciones();
        cargarMisGrupos();
        configurarDesplegables();
        configurarAutor();
        configurarSeleccionGrupo();
    }

    private void cargarUsuarioActual() {
        usuarioActualId = EntornoData.getEstudianteActual().id();
    }

    private void configurarAutor() {
        // Prellenar el campo autor con el usuario actual y hacerlo no editable
        String nombreUsuario = EntornoData.getEstudianteActual().nombre();
        textAutor.setText(nombreUsuario);
        textAutor.setEditable(false);
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

    private void configurarTablaPublicaciones() {
        // Configurar las columnas de la tabla de publicaciones
        ObservableList<TableColumn<PublicacionDTO, ?>> columnas = tablaContenidoPublicacionesGrupos.getColumns();

        if (columnas.size() >= 5) {
            columnaTitulo = (TableColumn<PublicacionDTO, String>) columnas.get(0);
            columnaAutor = (TableColumn<PublicacionDTO, String>) columnas.get(1);
            columnaMateria = (TableColumn<PublicacionDTO, String>) columnas.get(2);
            columnaFecha = (TableColumn<PublicacionDTO, LocalDateTime>) columnas.get(3);
            columnaArchivo = (TableColumn<PublicacionDTO, String>) columnas.get(4);

            // Configurar cada columna
            columnaTitulo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PublicacionDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<PublicacionDTO, String> param) {
                    return new SimpleStringProperty(param.getValue().titulo());
                }
            });

            columnaAutor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PublicacionDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<PublicacionDTO, String> param) {
                    return new SimpleStringProperty(param.getValue().publicador().nombre());
                }
            });

            columnaMateria.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PublicacionDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<PublicacionDTO, String> param) {
                    List<MateriaEstudio> materias = param.getValue().materias();
                    String materiasTexto = materias.isEmpty() ? "Sin materia" :
                            materias.stream().map(MateriaEstudio::name).collect(Collectors.joining(", "));
                    return new SimpleStringProperty(materiasTexto);
                }
            });

            columnaFecha.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PublicacionDTO, LocalDateTime>, ObservableValue<LocalDateTime>>() {
                @Override
                public ObservableValue<LocalDateTime> call(TableColumn.CellDataFeatures<PublicacionDTO, LocalDateTime> param) {
                    return new SimpleObjectProperty<>(param.getValue().fecha());
                }
            });

            // Formatear la fecha para mostrarla mejor
            columnaFecha.setCellFactory(new Callback<TableColumn<PublicacionDTO, LocalDateTime>, TableCell<PublicacionDTO, LocalDateTime>>() {
                @Override
                public TableCell<PublicacionDTO, LocalDateTime> call(TableColumn<PublicacionDTO, LocalDateTime> param) {
                    return new TableCell<PublicacionDTO, LocalDateTime>() {
                        @Override
                        protected void updateItem(LocalDateTime item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                setText(item.format(formatter));
                            }
                        }
                    };
                }
            });

            columnaArchivo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PublicacionDTO, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<PublicacionDTO, String> param) {
                    String rutaArchivo = param.getValue().archivo();
                    if (rutaArchivo != null && !rutaArchivo.isEmpty()) {
                        // Extraer solo el nombre del archivo de la ruta completa
                        File archivo = new File(rutaArchivo);
                        return new SimpleStringProperty(archivo.getName());
                    } else {
                        return new SimpleStringProperty("Sin archivo");
                    }
                }
            });
        }
    }

    private void configurarSeleccionGrupo() {
        // Agregar listener para cuando se seleccione un grupo en la tabla
        tablaMisGrupos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        cargarPublicacionesDelGrupo(newValue);
                    }
                }
        );
    }

    private void cargarPublicacionesDelGrupo(GrupoEstudioDTO grupoSeleccionado) {
        try {
            System.out.println("Cargando publicaciones del grupo: " + grupoSeleccionado.nombre());

            // Obtener las publicaciones del grupo usando el servicio
            List<PublicacionDTO> publicaciones = grupoService.obtenerPublicacionesDeGrupo(grupoSeleccionado.id());

            System.out.println("Publicaciones encontradas: " + publicaciones.size());

            // Cargar las publicaciones en la tabla
            tablaContenidoPublicacionesGrupos.getItems().setAll(publicaciones);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al cargar las publicaciones del grupo: " + e.getMessage());
            // Limpiar la tabla en caso de error
            tablaContenidoPublicacionesGrupos.getItems().clear();
        }
    }

    private void cargarMisGrupos() {
        // Obtener todos los grupos
        List<GrupoEstudioDTO> todosLosGrupos = grupoService.listarTodos();
        System.out.println(todosLosGrupos.toString());
        // Filtrar solo los grupos donde el usuario actual está presente
        misGrupos = todosLosGrupos.stream()
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

    private void configurarDesplegables() {
        // Configurar desplegable de materias (enum MateriaEstudio)
        if (desplegableMaterias != null) {
            desplegableMaterias.getItems().setAll(MateriaEstudio.values());
            System.out.println("Materias cargadas: " + MateriaEstudio.values().length);
        }

        // Configurar desplegable de grupos (usar misGrupos que ya fue cargado)
        if (desplegableGrupos != null) {
            System.out.println("Configurando desplegable de grupos...");
            System.out.println("misGrupos size: " + (misGrupos != null ? misGrupos.size() : "null"));

            if (misGrupos != null && !misGrupos.isEmpty()) {
                desplegableGrupos.getItems().clear(); // Limpiar items existentes
                desplegableGrupos.getItems().setAll(misGrupos);
                System.out.println("Grupos cargados en desplegable: " + desplegableGrupos.getItems().size());
            } else {
                desplegableGrupos.getItems().clear();
                System.out.println("No hay grupos para cargar en el desplegable");
            }

            // Configurar cómo se muestra cada grupo en el ComboBox
            desplegableGrupos.setCellFactory(param -> new ListCell<GrupoEstudioDTO>() {
                @Override
                protected void updateItem(GrupoEstudioDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.nombre());
                    }
                }
            });

            // Configurar cómo se muestra el grupo seleccionado
            desplegableGrupos.setButtonCell(new ListCell<GrupoEstudioDTO>() {
                @Override
                protected void updateItem(GrupoEstudioDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Elegir Grupo");
                    } else {
                        setText(item.nombre());
                    }
                }
            });
        } else {
            System.out.println("ERROR: desplegableGrupos es null");
        }
    }


    @FXML
    void publicar(ActionEvent event) {
        // Validar campos obligatorios
        if (textTituloContenido.getText().trim().isEmpty()) {
            mostrarAlerta("Error: El título es obligatorio");
            return;
        }

        if (desplegableGrupos.getValue() == null) {
            mostrarAlerta("Error: Debe seleccionar un grupo");
            return;
        }

        try {
            // Obtener datos del formulario
            String titulo = textTituloContenido.getText().trim();
            GrupoEstudioDTO grupoSeleccionado = desplegableGrupos.getValue();

            // Crear lista de materias
            List<MateriaEstudio> materias = new ArrayList<>();
            if (desplegableMaterias.getValue() != null) {
                materias.add(desplegableMaterias.getValue());
            }

            // Obtener estudiante actual
            EstudianteDTO estudianteActual = EntornoData.getEstudianteActual();

            // Crear el DTO de la publicación
            PublicacionDTO publicacionDTO = new PublicacionDTO(
                    null, // ID será generado automáticamente
                    estudianteActual,
                    LocalDateTime.now(),
                    titulo,
                    materias,
                    new ArrayList<>(), // Sin valoraciones iniciales
                    archivoSeleccionado != null ? archivoSeleccionado.getAbsolutePath() : null
            );

            // Crear la publicación en el grupo usando el método estático
            boolean exito = PublicacionService.crearPublicacionEnGrupo(publicacionDTO, grupoSeleccionado.id());

            if (exito) {
                mostrarAlerta("Publicación creada exitosamente en el grupo: " + grupoSeleccionado.nombre());
                limpiarCampos();
            } else {
                mostrarAlerta("Error al crear la publicación");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado: " + e.getMessage());
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
    void subirArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona un archivo para subir");
        archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            mostrarAlerta("Archivo cargado: " + archivoSeleccionado.getName());
        }
    }

    @FXML
    void abandonarGrupo(ActionEvent event) {
        // Obtener el grupo seleccionado de la tabla
        GrupoEstudioDTO grupoSeleccionado = tablaMisGrupos.getSelectionModel().getSelectedItem();

        if (grupoSeleccionado == null) {
            mostrarAlerta("Por favor, seleccione un grupo para abandonar");
            return;
        }

        try {
            // Confirmar la acción con el usuario
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar abandono");
            confirmacion.setHeaderText("¿Está seguro que desea abandonar el grupo?");
            confirmacion.setContentText("Grupo: " + grupoSeleccionado.nombre());

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Remover al estudiante del grupo usando el servicio
                boolean exito = grupoService.removerEstudianteDeGrupo(grupoSeleccionado.id(), usuarioActualId);

                if (exito) {
                    mostrarAlerta("Has abandonado exitosamente el grupo: " + grupoSeleccionado.nombre());

                    // Actualizar la tabla local removiendo el grupo
                    misGrupos.removeIf(grupo -> grupo.id().equals(grupoSeleccionado.id()));
                    tablaMisGrupos.getItems().setAll(misGrupos);

                    // Limpiar la tabla de publicaciones ya que no hay grupo seleccionado
                    tablaContenidoPublicacionesGrupos.getItems().clear();

                    // Navegar de vuelta a la vista de GruposUsuario
                    irAInicio(event);

                } else {
                    mostrarAlerta("Error al abandonar el grupo. Inténtelo nuevamente.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error inesperado al abandonar el grupo: " + e.getMessage());
        }
    }
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setContentText(mensaje);
        alerta.show();
    }

    private void limpiarCampos() {
        textTituloContenido.clear();
        textAutor.clear();
        desplegableMaterias.setValue(null);
        date.setValue(null);
        archivoSeleccionado = null;
    }
}
