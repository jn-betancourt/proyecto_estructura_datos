package com.bindr;

import java.io.IOException;

import com.bindr.dto.RegistroRequestDTO;
import com.bindr.persistencia.HibernateConfig;
import com.bindr.servicios.AutenticacionService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        var reg = new RegistroRequestDTO("juan", "juan@mail", "123");
        AutenticacionService ser = new AutenticacionService();
        var nuevo = ser.registrar(reg);
        System.out.println(nuevo.correo() +" - "+ nuevo.id()); // EJEMPLO DE COMO SE PUEDE UTULIZAR EL SERVICIO
        var reg2 = new RegistroRequestDTO("juan1", "jua1n@mail", "123");
        ser.registrar(reg2);
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
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
}