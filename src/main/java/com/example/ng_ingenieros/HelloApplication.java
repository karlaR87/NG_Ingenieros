package com.example.ng_ingenieros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Asistencia_visualizar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600); //width, heigth
        stage.setTitle("NG Ingenieros");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }

}