package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.HelloApplication;
import com.example.ng_ingenieros.Validaciones;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.ng_ingenieros.Validaciones;

import javax.swing.*;

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

    @FXML
    private Label lbMensaje;

    //validaciones
    public static boolean validarNumero(String input) {
        return input.matches("\\d+");
    }

    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z]+");
    }

    public static boolean validarCorreo(String input) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean NoVacio(String input) {
        return !input.trim().isEmpty();
    }


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
        validaciones();

    }

    public void registrardatos(){

        String nombre = txtNombre.getText();
        String dui = txtDui.getText();
        String correo = txtCorreoE.getText();
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();

         //ahora crea un String para hacer la insercion
        String Insercion = "insert into tbempleados(nombreCompleto, dui, correo) values(?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, dui);
            preparedStatement.setString(3, correo);
            preparedStatement.executeUpdate();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RegistrarseSegundo.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Registrarse");

                stage.setScene(new Scene(root));
                stage.show();

                // Opcional: Cerrar la ventana actual
                ((Stage) txtNombre.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();

                lbMensaje.setText("Credenciales inválidas");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void validaciones() {
        if (validarLetras(txtNombre.getText())) {
            if (validarCorreo(txtCorreoE.getText())){
                if (validarNumero(txtDui.getText())){
                    registrardatos();
                }
                else {
                    mostrarAlerta("Error de Validación", "Ingrese solo números.");
                }
            }
            else {
                mostrarAlerta("Error de Validación", "Ingrese un correo válido.");

            }  mostrarAlerta("Validación Exitosa", "Letras válidas.");
        } else {
            mostrarAlerta("Error de Validación", "Ingrese solo letras.");
        }
    }

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
