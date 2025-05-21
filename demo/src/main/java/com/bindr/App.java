package com.bindr;

import com.bindr.modelos.Estudiante;
import com.bindr.persistencia.HibernateConfig;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/VistaLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Bindr");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        EntityManager manager = HibernateConfig.getEntityManager();
        try{
            manager.getTransaction().begin();
            //Estudiante est = new Estudiante("juan", "j", "juanito2006");
            //manager.persist(est);
            //manager.getTransaction().commit();
        }catch (Exception e){
            manager.getTransaction().rollback();
            e.printStackTrace(System.out);
        }

        launch();
    }
}
