package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecuperarContraseñaDosControlador {
    @FXML
    private TextField txtCodigoRecu;
    @FXML
    private Button btnRegresar, btnReenviarCodigo, btnSiguiente;

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

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    public void initialize() {
        // Configura el evento de clic para el botón
        btnRegresar.setOnAction(this::btnRegresarOnAction);
        btnReenviarCodigo.setOnAction(this::BtnSiguienteOnAction);
        btnSiguiente.setOnAction(this::BtnSiguienteOnAction);

    }

    // Método para abrir una nueva ventana
    private void btnRegresarOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RecuperacionContraseña.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Recuperar contraseña");


            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BtnSiguienteOnAction(ActionEvent event) {
        validaciones();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RecuperarContraseñaTres.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Recuperar contraseña");


            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validaciones() {
        if (NoVacio(txtCodigoRecu.getText())){

        } else {
            mostrarAlerta("Error de Validación", "Ingresar datos, no pueden haber campos vacíos.");
        }
    }




}
