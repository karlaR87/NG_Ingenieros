package com.example.ng_ingenieros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuPrincipal.fxml"));
        //ESTA PARTE COMENTADA ES LA NORMAL POR DEFECTO
        /*Scene scene = new Scene(fxmlLoader.load(), 750, 500); //width, heigth
        stage.setTitle("NG Ingenieros");
        stage.setScene(scene);
        stage.show();*/


        //TODO ESTE CODIGO ES PARA QUE SALGA EN PANTALLA COMPLETA LA VENTANA
        Parent root = fxmlLoader.load();
        // Crear una escena con tu BorderPane como raíz
        Scene scene = new Scene(root);
        // Configurar la escena en el escenario (ventana)
        stage.setScene(scene);
        stage.setTitle("Tu Aplicación"); // Título de la ventana
        stage.setFullScreen(true); // Pantalla completa
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }

}