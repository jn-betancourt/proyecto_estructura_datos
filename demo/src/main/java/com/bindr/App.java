package com.bindr;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import com.bindr.dto.EstudianteDTO;
import com.bindr.dto.PublicacionDTO;
import com.bindr.dto.RegistroRequestDTO;
import com.bindr.dto.ValoracionDTO;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.persistencia.HibernateConfig;
import com.bindr.servicios.AutenticacionService;
import com.bindr.servicios.PublicacionService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        HibernateConfig.getEntityManager();
        simularValoracionDePublicacion();
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("VistaLogin"), 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        HibernateConfig.shutdown();
    }
    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
         FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource(fxml+".fxml"));
         return fxmlLoader.load();
    }

    public static void simularValoracionDePublicacion() {
    // 1. Crear usuario
    AutenticacionService authService = new AutenticacionService();
    String correo = "valorador@demo.com";
    String nombre = "Valorador Demo";
    String password = "demo123";
    EstudianteDTO usuario = authService.registrar(new RegistroRequestDTO(nombre, correo, password));
    if (usuario == null) throw new RuntimeException("No se pudo crear el usuario");

    // 2. Crear publicación
    PublicacionDTO publicacion = new PublicacionDTO(
            null,
            usuario,
            null,
            "Publicación de prueba",
            List.of(MateriaEstudio.BIOLOGIA),
            List.of(), // Sin valoraciones al inicio
            null
    );
    boolean creada = PublicacionService.crearPublicacion(publicacion);
    if (!creada) throw new RuntimeException("No se pudo crear la publicación");

    // 3. Buscar la publicación creada
    List<PublicacionDTO> publicaciones = PublicacionService.obtenerPorIdDeUsuario(usuario.id());
    PublicacionDTO publicada = publicaciones.stream()
            .filter(p -> "Publicación de prueba".equals(p.titulo()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No se encontró la publicación"));

    // 4. Crear valoración y agregarla
    ValoracionDTO valoracion = new ValoracionDTO(
            null,
            usuario.correo(),
            true, // Like
            java.time.LocalDate.now()
    );
    List<ValoracionDTO> nuevasValoraciones = new ArrayList<>(publicada.valoraciones());
    nuevasValoraciones.add(valoracion);

    // 5. Actualizar la publicación con la nueva valoración
    PublicacionDTO actualizada = new PublicacionDTO(
            publicada.id(),
            publicada.publicador(),
            publicada.fecha(),
            publicada.titulo(),
            publicada.materias(),
            nuevasValoraciones,
            publicada.archivo()
    );
    boolean actualizadaOk = PublicacionService.actualizarPublicacion(actualizada);
    if (!actualizadaOk) throw new RuntimeException("No se pudo actualizar la publicación con la valoración");

    // 6. Verificar
    PublicacionDTO verificada = PublicacionService.obtenerPorIdDeUsuario(usuario.id()).stream()
            .filter(p -> p.id().equals(actualizada.id()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No se encontró la publicación actualizada"));
    System.out.println("Valoraciones en la publicación: " + verificada.valoraciones().size());
}
}