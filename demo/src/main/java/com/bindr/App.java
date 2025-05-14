package com.bindr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.bindr.modelos.Estudiante;
import com.bindr.persistencia.HibernateConfig;

import jakarta.persistence.EntityManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // scene = new Scene(loadFXML("primary"), 640, 480);
        // stage.setScene(scene);
        // stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        // scene.setRoot(loadFXML(fxml));
    }

    // private static Parent loadFXML(String fxml) throws IOException {
    //     // FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    //     // return fxmlLoader.load();
    // }

    public static void main(String[] args) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try{
            manager.getTransaction().begin();
            Estudiante est = new Estudiante("juan", "j", "juanito2006");
            manager.persist(est);
            manager.getTransaction().commit();
        }catch (Exception e){
            manager.getTransaction().rollback();
            e.printStackTrace(System.out);
        }    

        launch();
    }

}