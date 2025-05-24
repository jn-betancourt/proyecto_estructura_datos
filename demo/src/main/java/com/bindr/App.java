package com.bindr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.bindr.dao.EstudianteDao;
import com.bindr.dao.PublicacionDao;
import com.bindr.modelos.Estudiante;
import com.bindr.modelos.MateriaEstudio;
import com.bindr.modelos.Publicacion;
import com.bindr.persistencia.HibernateConfig;

import jakarta.persistence.EntityManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        ejemplo(args);
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Estudiante est = Estudiante.builder().correo("1@mail").nombre("prueba1").contrasena("123").build();
        // EstudianteDao.crearEstudiante(est);
        // scene = new Scene(loadFXML("primary"), 640, 480);
        // stage.setScene(scene);
        // stage.show();
    }

    @Override
    public void stop(){
        HibernateConfig.shutdown();
    }
    
    static void setRoot(String fxml) throws IOException {
        // scene.setRoot(loadFXML(fxml));
    }

    // private static Parent loadFXML(String fxml) throws IOException {
    //     // FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    //     // return fxmlLoader.load();
    // }
    public static void ejemplo(String[] args) {
        // Crear estudiante
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Ana Torres");
        estudiante.setCorreo("ana.torres@ejemplo.com");
        estudiante.setContraseña("secreta123");

        if (EstudianteDao.crearEstudiante(estudiante)) {
            System.out.println("Estudiante creado con ID: " + estudiante.getId());
        }

        // Crear publicación
        Publicacion publicacion = new Publicacion.Builder()
            .publicador(estudiante)
            .fecha(LocalDateTime.now())
            .titulo("Busco grupo para repasar estructuras de datos")
            .materias(List.of(MateriaEstudio.BIOLOGIA, MateriaEstudio.INFORMATICA))
            .build();

        // Guardar la publicación
        if (PublicacionDao.crear(publicacion)) {
            System.out.println("Publicación guardada correctamente con ID: " + publicacion.getId());
        }

        // Recuperar y mostrar
        Publicacion recuperada = PublicacionDao.buscarPorId(publicacion.getId());
        if (recuperada != null) {
            System.out.println("Título: " + recuperada.getTitulo());
            System.out.println("Fecha: " + recuperada.getFecha());
            System.out.println("Publicador: " + recuperada.getPublicador().getNombre());
            System.out.println("Materias: " + recuperada.getMaterias());
        } else {
            System.out.println("No se pudo recuperar la publicación.");
        }
    }

}