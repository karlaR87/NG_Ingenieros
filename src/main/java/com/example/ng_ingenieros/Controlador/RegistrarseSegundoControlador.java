package com.example.ng_ingenieros.Controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegistrarseSegundoControlador {
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private PasswordField txtConfirmaContra;
    @FXML
    private TextField txtUsuario;

    @FXML
    private Button btnRegistrarse;


    public void initialize() {
        // Configura el evento de clic para el botón
        btnRegistrarse.setOnAction(this::btnRegistrarseOnAction);


    }


    private void btnRegistrarseOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrarse");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
