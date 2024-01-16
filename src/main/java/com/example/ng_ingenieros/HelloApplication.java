package com.example.ng_ingenieros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloApplication extends Application {

    @Override

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 727, 458); //width, heigth
        stage.setTitle("NG Ingenieros");
        stage.setScene(scene);
        stage.show();
    }
            /*Conexion conexion = new Conexion();
            Connection connection = conexion.obtenerConexion();

            String verifylogin = "SELECT COUNT(1) FROM tbusuarios";
            try {

                PreparedStatement preparedStatement = connection.prepareStatement(verifylogin);
                ResultSet queryResult = preparedStatement.executeQuery();


                while(queryResult.next()){
                    if(queryResult.getInt(1)==0){
                        try {


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{


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
*/
}