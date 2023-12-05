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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RecuperarContraseñaTresControlador {
    @FXML
    private TextField txtNuevaContra, txtConfirmarContra;
    @FXML
    private Button btnSiguiente;

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
    private static String correoRecuperado;
    public static void setCorreoRecuperadoEnviado(String correo) {
        correoRecuperado = correo;
    }



    public void initialize() {
        // Configura el evento de clic para el botón

        btnSiguiente.setOnAction(this::BtnSiguienteOnAction);

    }
    

    private void actualizarcontraseña() {
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        String Insercion = "UPDATE tbusuarios SET contraseña = ? WHERE idempleado = (SELECT idempleado FROM tbempleados WHERE correo = ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, txtNuevaContra.getText());
            preparedStatement.setString(2, correoRecuperado);
            preparedStatement.executeUpdate();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Registrarse");


                stage.setScene(new Scene(root));
                ((Stage) txtNuevaContra.getScene().getWindow()).close();
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Resto del código para cambiar a la ventana de Login...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para abrir una nueva ventana
    private void BtnSiguienteOnAction(ActionEvent event) {
        validaciones();
    }

    public void validaciones() {
       if (NoVacio(txtNuevaContra.getText())){
           actualizarcontraseña();
       }else {
           mostrarAlerta("Error de Validación", "Ingresar datos, no pueden haber campos vacíos.");

       }
    }

}
