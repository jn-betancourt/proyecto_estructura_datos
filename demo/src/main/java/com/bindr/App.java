package com.bindr;

import java.io.IOException;

import com.bindr.dto.ConversacionDTO;
import com.bindr.persistencia.HibernateConfig;
import com.bindr.servicios.MensajeService;

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
}