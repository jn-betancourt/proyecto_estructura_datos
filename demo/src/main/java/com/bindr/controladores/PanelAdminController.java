package com.bindr.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import com.bindr.dto.EstudianteDTO;
import com.bindr.modelos.Publicacion;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import com.bindr.servicios.ModeradorService;

public class PanelAdminController {

    // Servicios (puedes agregar más según lo que uses)
    private final ModeradorService moderadorService = new ModeradorService();

    // Botones gestión usuarios
    @FXML private Button btnListarUsuarios;
    @FXML private Button btnAgregarUsuario;
    @FXML private Button btnEliminarUsuario;

    // Botones gestión contenidos
    @FXML private Button btnListarPublicaciones;
    @FXML private Button btnEliminarPublicacion;

    // Tablas y columnas usuarios
    @FXML private TableView<EstudianteDTO> tablaUsuarios;
    @FXML private TableColumn<EstudianteDTO, Long> colUsuarioId;
    @FXML private TableColumn<EstudianteDTO, String> colUsuarioNombre;
    @FXML private TableColumn<EstudianteDTO, String> colUsuarioCorreo;

    // Tablas y columnas publicaciones
    @FXML private TableView<Publicacion> tablaPublicaciones;
    @FXML private TableColumn<Publicacion, Long> colPubId;
    @FXML private TableColumn<Publicacion, String> colPubTitulo;
    @FXML private TableColumn<Publicacion, String> colPubAutor;

    // Tablas y columnas reportes
    @FXML private TableView<Publicacion> tablaContenidosValorados;
    @FXML private TableColumn<Publicacion, String> colTitulo;
    @FXML private TableColumn<Publicacion, Integer> colValoraciones;

    @FXML private TableView<EstudianteDTO> tablaEstudiantesConexiones;
    @FXML private TableColumn<EstudianteDTO, String> colNombreEstudiante;
    @FXML private TableColumn<EstudianteDTO, Integer> colConexiones;

    @FXML private TextField txtEstudianteOrigen;
    @FXML private TextField txtEstudianteDestino;
    @FXML private Button btnBuscarCamino;
    @FXML private ListView<String> listaCaminoCorto;

    @FXML private ListView<String> listaComunidades;

    @FXML private TableView<ParticipacionRow> tablaParticipacion;
    @FXML private TableColumn<ParticipacionRow, String> colEstParticipacion;
    @FXML private TableColumn<ParticipacionRow, Integer> colNivelParticipacion;

    @FXML private TabPane tabPaneReportes;

    @FXML private Pane grafoPane;

    // Métodos de inicialización y eventos
    @FXML
    public void initialize() {
        colUsuarioId.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().id()).asObject());
        colUsuarioNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().nombre()));
        colUsuarioCorreo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().correo()));

        // Inicializar columnas de publicaciones
        colPubId.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        colPubTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        colPubAutor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getPublicador() != null ? data.getValue().getPublicador().getNombre() : ""
        ));

        // Inicializar columnas de reportes
        colTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        colValoraciones.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(
            data.getValue().getValoraciones() != null ? data.getValue().getValoraciones().size() : 0
        ).asObject());

        colNombreEstudiante.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().nombre()));
        colConexiones.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(
            moderadorService.obtenerGrafoAfinidad().getOrDefault(data.getValue(), Map.of()).size()
        ).asObject());

        colEstParticipacion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstudiante()));
        colNivelParticipacion.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNivel()).asObject());

        // Inicializar datos de reportes automáticamente
        onListarContenidosMasValorados();
        onListarEstudiantesConMasConexiones();
        onListarComunidades();
        onListarNivelesParticipacion();
        dibujarGrafoAfinidad();
    }

    // Ejemplo de método para un botón
    @FXML
    private void onListarUsuarios() {
        tablaUsuarios.getItems().clear();
        tablaUsuarios.getItems().addAll(moderadorService.listarUsuarios());
        dibujarGrafoAfinidad();
    }

    @FXML
    private void onEliminarUsuario() {
        EstudianteDTO seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Debes seleccionar un usuario para eliminar.");
            return;
        }
        boolean eliminado = moderadorService.eliminarUsuario(seleccionado.id());
        if (eliminado) {
            tablaUsuarios.getItems().remove(seleccionado);
            mostrarAlerta("Usuario eliminado correctamente.");
            dibujarGrafoAfinidad();
        } else {
            mostrarAlerta("No se pudo eliminar el usuario.");
        }
    }

    @FXML
    private void onAgregarUsuario() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Ingrese nombre, correo y contraseña separados por coma");
        dialog.setContentText("Ejemplo: Juan Perez,juan@mail.com,1234");
        dialog.showAndWait().ifPresent(input -> {
            String[] datos = input.split(",");
            if (datos.length == 3) {
                var registro = new com.bindr.dto.RegistroRequestDTO(datos[0].trim(), datos[1].trim(), datos[2].trim());
                var nuevo = new com.bindr.servicios.AutenticacionService().registrar(registro);
                if (nuevo != null) {
                    tablaUsuarios.getItems().add(nuevo);
                    mostrarAlerta("Usuario agregado correctamente.");
                    dibujarGrafoAfinidad();
                } else {
                    mostrarAlerta("No se pudo agregar el usuario (¿correo ya existente?).");
                }
            } else {
                mostrarAlerta("Formato incorrecto.");
            }
        });
    }

    @FXML
    private void onListarPublicaciones() {
        // Ejemplo: listar todas las publicaciones (puedes ajustar el tipo de tablaPublicaciones y columnas)
        List<Publicacion> publicaciones = com.bindr.dao.PublicacionDao.listarTodas();
        tablaPublicaciones.getItems().setAll(publicaciones);
    }

    @FXML
    private void onEliminarPublicacion() {
        Publicacion seleccionada = (Publicacion) tablaPublicaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Debes seleccionar una publicación para eliminar.");
            return;
        }
        com.bindr.dao.PublicacionDao.eliminar(seleccionada.getId());
        tablaPublicaciones.getItems().remove(seleccionada);
        mostrarAlerta("Publicación eliminada correctamente.");
    }

    @FXML
    private void onListarContenidosMasValorados() {
        List<Publicacion> top = moderadorService.obtenerContenidosMasValorados(10);
        tablaContenidosValorados.setItems(FXCollections.observableArrayList(top));
    }

    @FXML
    private void onListarEstudiantesConMasConexiones() {
        List<EstudianteDTO> top = moderadorService.obtenerEstudiantesConMasConexiones(10);
        tablaEstudiantesConexiones.setItems(FXCollections.observableArrayList(top));
        dibujarGrafoAfinidad();
    }

    @FXML
    private void onBuscarCamino() {
        String origen = txtEstudianteOrigen.getText().trim();
        String destino = txtEstudianteDestino.getText().trim();
        if (origen.isEmpty() || destino.isEmpty()) {
            mostrarAlerta("Debes ingresar ambos correos.");
            return;
        }
        EstudianteDTO estOrigen = moderadorService.listarUsuarios().stream()
                .filter(e -> e.correo().equalsIgnoreCase(origen)).findFirst().orElse(null);
        EstudianteDTO estDestino = moderadorService.listarUsuarios().stream()
                .filter(e -> e.correo().equalsIgnoreCase(destino)).findFirst().orElse(null);
        if (estOrigen == null || estDestino == null) {
            mostrarAlerta("Uno o ambos estudiantes no existen.");
            return;
        }
        List<EstudianteDTO> camino = moderadorService.obtenerCaminoMasCorto(estOrigen, estDestino);
        listaCaminoCorto.setItems(FXCollections.observableArrayList(
            camino.stream().map(EstudianteDTO::nombre).toList()
        ));
    }

    @FXML
    private void onListarComunidades() {
        List<Set<EstudianteDTO>> comunidades = moderadorService.detectarComunidades();
        List<String> comunidadesStr = comunidades.stream()
            .map(set -> set.stream().map(EstudianteDTO::nombre).toList().toString())
            .toList();
        listaComunidades.setItems(FXCollections.observableArrayList(comunidadesStr));
        dibujarGrafoAfinidad();
    }

    @FXML
    private void onListarNivelesParticipacion() {
        Map<EstudianteDTO, Integer> participacion = moderadorService.obtenerNivelesParticipacion();
        List<ParticipacionRow> rows = participacion.entrySet().stream()
            .map(e -> new ParticipacionRow(e.getKey().nombre(), e.getValue()))
            .toList();
        tablaParticipacion.setItems(FXCollections.observableArrayList(rows));
    }

    // Visualización gráfica del grafo de afinidad
    private void dibujarGrafoAfinidad() {
        grafoPane.getChildren().clear();
        Map<EstudianteDTO, Map<EstudianteDTO, Integer>> grafo = moderadorService.obtenerGrafoAfinidad();
        int n = grafo.size();
        if (n == 0) return;

        double centerX = 200, centerY = 100, radius = 80;
        EstudianteDTO[] nodos = grafo.keySet().toArray(new EstudianteDTO[0]);
        Map<EstudianteDTO, Circle> circulos = new java.util.HashMap<>();

        // Dibujar nodos en círculo
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            Circle c = new Circle(x, y, 18, Color.LIGHTBLUE);
            c.setStroke(Color.DARKBLUE);
            grafoPane.getChildren().add(c);
            Text t = new Text(x - 15, y + 5, nodos[i].nombre());
            grafoPane.getChildren().add(t);
            circulos.put(nodos[i], c);
        }
        // Dibujar aristas
        for (int i = 0; i < n; i++) {
            EstudianteDTO origen = nodos[i];
            Circle c1 = circulos.get(origen);
            double x1 = c1.getCenterX(), y1 = c1.getCenterY();
            for (EstudianteDTO destino : grafo.get(origen).keySet()) {
                if (origen.equals(destino)) continue;
                Circle c2 = circulos.get(destino);
                if (c2 == null) continue;
                double x2 = c2.getCenterX(), y2 = c2.getCenterY();
                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(Color.GRAY);
                grafoPane.getChildren().add(0, line); // Dibuja líneas detrás de los nodos
            }
        }
    }

    // Clase auxiliar para la tabla de participación
    public static class ParticipacionRow {
        private final String estudiante;
        private final Integer nivel;
        public ParticipacionRow(String estudiante, Integer nivel) {
            this.estudiante = estudiante;
            this.nivel = nivel;
        }
        public String getEstudiante() { return estudiante; }
        public Integer getNivel() { return nivel; }
    }

    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void refrescarUsuarios() {
        tablaUsuarios.getItems().setAll(moderadorService.listarUsuarios());
    }

    // Agrega aquí los métodos para los demás botones y lógica de interacción

}
