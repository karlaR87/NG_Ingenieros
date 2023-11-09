package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegistrarseControlador {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreoE;

    @FXML
    private TextField txtDui;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnIniciarSesion;

    public void initialize() {
        // Configura el evento de clic para el botón
        btnIniciarSesion.setOnAction(this::btnIniciarSesionOnAction);
        btnSiguiente.setOnAction(this::BtnSiguienteOnAction);

    }

    // Método para abrir una nueva ventana
    private void btnIniciarSesionOnAction(ActionEvent event) {
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

    private void BtnSiguienteOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RegistrarseSegundo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrarse");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registrardatos(){
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();


//hora crea un String para hacer la insercion
        String Insercion = "insert into tbempleados(nombreCompleto, dui, correo) values(?,?,?);";
    }









}
