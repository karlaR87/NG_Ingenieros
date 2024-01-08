package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
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

public class PrimerUsoEMPControlador {

    @FXML
    private TextField txtNombre, txtDui, txtCorreo;
    @FXML
    private Button btnAceptar;
    public static boolean validarNumero(String input) {
        return input.matches("\\d+");
    }

    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z ]+");
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
        btnAceptar.setOnAction(this::btnAceptarOnAction);
    }
    // Método para abrir una nueva ventana

    private void btnAceptarOnAction(ActionEvent event){
        validaciones();
    }
    public void registrardatos(){
        String nombre = txtNombre.getText();
        String dui = txtDui.getText();
        String correo = txtCorreo.getText();
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        PrimerUsoControlador.setnombre(nombre);

        //ahora crea un String para hacer la insercion
        String Insercion = "insert into tbempleados(nombreCompleto, dui, correo) values(?,?,?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, dui);
            preparedStatement.setString(3, correo);
            preparedStatement.executeUpdate();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/PrimerUso.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Registrarse");

                stage.setScene(new Scene(root));
                stage.show();

                // Opcional: Cerrar la ventana actual
                ((Stage) txtNombre.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void validaciones() {
        if (NoVacio(txtNombre.getText()) && NoVacio(txtCorreo.getText()) && NoVacio(txtDui.getText())) {
            if (validarDui(txtDui.getText()))
                if (validarLetras(txtNombre.getText())){
                    if (validarCorreo(txtCorreo.getText())){
                        registrardatos();

                    }
                    else {
                        mostrarAlerta("Error de Validación", "Ingrese un correo válido.");
                    }
                }
                else {
                    mostrarAlerta("Error de Validación", "Ingrese solo letras.");
                }
            else {
                mostrarAlerta("Error de Validación", "Ingrese un DUI válido.");

            }
        }
        else {
            mostrarAlerta("Error de validación", "Ingresar datos, no pueden haber campos vacíos.");
        }
    }

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Función para validar el formato de un DUI (por ejemplo, 12345678-9)
    private boolean validarDui(String dui) {
        // Se puede implementar una lógica más avanzada según el formato real de DUI
        return dui.matches("\\d{8}-\\d{1}");
    }

}
