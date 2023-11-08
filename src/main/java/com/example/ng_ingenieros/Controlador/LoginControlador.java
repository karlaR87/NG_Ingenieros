package com.example.ng_ingenieros.Controlador;

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

import java.io.IOException;

public class LoginControlador {

    @FXML
    private Button btnRegistrar;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private Button btnIngresar;

    public void initialize() {
        // Configura el evento de clic para el botón
        btnIngresar.setOnAction(this::btnIngresarOnAction);
        btnRegistrar.setOnAction(this::btnRegistrarOnAction);
    }

    // Método para abrir una nueva ventana
    private void btnIngresarOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/MenuPrincipal.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nueva Ventana");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void loadWindow(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
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

