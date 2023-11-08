package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginControlador {

    @FXML
    private Button btnRegistrar;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private Button btnIngresar;
    @FXML
    private Label lbmensaje;

    public void initialize() {
        // Configura el evento de clic para el botón
        btnIngresar.setOnAction(this::btnIngresarOnAction);
        btnRegistrar.setOnAction(this::btnRegistrarOnAction);
    }

    // Método para abrir una nueva ventana
    private void btnIngresarOnAction(ActionEvent event) {



        if (txtUsuario.getText().isBlank() == false && txtContraseña.getText().isBlank() == false){
        }

    }

    private void btnRegistrarOnAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Registrarse.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nueva");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/MenuPrincipal.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Inicio");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void validatelogin(){
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();

        String verifylogin = "Select count(1) FROM tbusuarios where nombreUsuario = '"+ txtUsuario.getText()+ "' and contraseña =' "+ txtContraseña.getText() +"'";
        try {

            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(verifylogin);


            while(queryResult.next()){
                if(queryResult.getInt(1)==1){
                    loadWindow();
                    lbmensaje.setText("hola");
                }
                else{

                    lbmensaje.setText("Credenciales no validas");
                }

            }


        }
        catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }
   /* public void btnIngresarOnAction(ActionEvent event) {
        String user = txtUsuario.getText();
        String contra = txtContraseña.getText();
     //   loadWindow("/com/example/ng_ingenieros/MenuPrincipal.fxml");
        if (user == "elmer" && contra == "hola123") {
            loadWindow("/com/example/ng_ingenieros/MenuPrincipal.fxml");

        }*/


    }

