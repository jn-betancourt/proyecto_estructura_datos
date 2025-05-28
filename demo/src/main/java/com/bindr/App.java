package com.bindr;

import java.io.IOException;

import com.bindr.dao.ConversacionDao;
import com.bindr.dao.EstudianteDao;
import com.bindr.dao.GrupoEstudioDao;
import com.bindr.dao.ModeradorDao;
import com.bindr.dao.PublicacionDao;
import com.bindr.dao.SolicitudAyudaDao;
import com.bindr.dto.ModeradorDTO;
import com.bindr.modelos.Conversacion;
import com.bindr.modelos.Moderador;
import com.bindr.persistencia.HibernateConfig;
import com.bindr.servicios.AfinidadService;
import com.bindr.servicios.ModeradorService;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

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
        AfinidadService afinidadService = new AfinidadService();
        afinidadService.reconstruirGrafo(
            EstudianteDao.listarTodos(),
            SolicitudAyudaDao.obtenerTodas(),
            GrupoEstudioDao.listarTodos(),
            PublicacionDao.listarTodas(),
            ConversacionDao.listarTodas()
        );
        HibernateConfig.getEntityManager();
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("VistaLogin"), 600, 410);
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