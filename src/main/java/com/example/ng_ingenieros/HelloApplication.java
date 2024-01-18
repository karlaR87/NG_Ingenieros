package com.example.ng_ingenieros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
            Conexion conexion = new Conexion();
            Connection connection = conexion.obtenerConexion();

            String verifylogin = "SELECT COUNT(1) FROM tbusuarios";
            try {

                PreparedStatement preparedStatement = connection.prepareStatement(verifylogin);
                ResultSet queryResult = preparedStatement.executeQuery();


                while(queryResult.next()){
                    if(queryResult.getInt(1)==0){
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("PrimerUsoEMP.fxml"));
                            Scene scene = new Scene(fxmlLoader.load(), 780, 450); //width, heigth
                            // Configurar el estilo para quitar la barra de título
                            stage.initStyle(StageStyle.UNDECORATED);

                            stage.setTitle("NG Ingenieros");
                            stage.setScene(scene);
                            stage.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 800, 458); //width, heigth
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(scene);
                        stage.show();

                    }
                }


            }
            catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }

        }
    public static void main(String[] args) {
        launch();
    }

}