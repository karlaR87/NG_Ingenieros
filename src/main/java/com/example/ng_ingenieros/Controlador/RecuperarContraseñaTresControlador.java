package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    @FXML
    private Button btnClose;

    @FXML
    private Pane topPane; // Asegúrate de que tienes una referencia a tu AnchorPane principal
    private double xOffset =0;
    private double yOffset =0;
    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() + (stage.getWidth() / 2) - event.getX();
        yOffset = stage.getY() - event.getY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset - (stage.getWidth() / 2));
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    protected void HandleCloseAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }



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

    private static String contraseñaEncriptada;

    public void initialize() {
        // Configura el evento de clic para el botón

        btnSiguiente.setOnAction(this::BtnSiguienteOnAction);

    }
    public void contraseña()  {
        String contraseña = txtNuevaContra.getText();
        contraseñaEncriptada = encriptarContraseña(contraseña);
        System.out.println("Contraseña original: " + contraseña);
        System.out.println("Contraseña encriptada: " + contraseñaEncriptada);
    }

    private void actualizarcontraseña() {
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        contraseña();
        String Insercion = "UPDATE tbusuarios SET contraseña = ? WHERE idempleado = (SELECT idempleado FROM tbempleados WHERE correo = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, contraseñaEncriptada);
            preparedStatement.setString(2, correoRecuperado);
            preparedStatement.executeUpdate();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);


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
        if (txtConfirmarContra.getText().equals(txtNuevaContra.getText())) {
            if (validarLongitud(txtNuevaContra.getText(), 8, 20)) {

                if (NoVacio(txtNuevaContra.getText())){
           actualizarcontraseña();
       }else {
           CustomAlert customAlert = new CustomAlert();
           customAlert.mostrarAlertaPersonalizada("Error", "Ingresar datos, no pueden haber campos vacíos.", (Stage) btnSiguiente.getScene().getWindow());
           return;
       }
            } else {
                CustomAlert customAlert = new CustomAlert();
                customAlert.mostrarAlertaPersonalizada("Error", "La longitud de la contraseña debe estar entre 8 y 20 caracteres.", (Stage) btnSiguiente.getScene().getWindow());
                return;
            }

        } else {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Las contraseñas no coinciden", (Stage) btnSiguiente.getScene().getWindow());
            return;            }
    }
    public static String encriptarContraseña(String contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseña.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexHash = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean validarLongitud(String texto, int minimo, int maximo) {
        int longitud = texto.length();
        return (longitud >= minimo && longitud <= maximo);
    }

}
