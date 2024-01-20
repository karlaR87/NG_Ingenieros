package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

    //validaciones
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
        btnIniciarSesion.setOnAction(this::btnIniciarSesionOnAction);
        btnSiguiente.setOnAction(this::BtnSiguienteOnAction);
    }


    // Método para abrir una nueva ventana
    private void btnIniciarSesionOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);


            stage.setScene(new Scene(root));
            ((Stage) txtNombre.getScene().getWindow()).close();
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
        RegistrarseSegundoControlador.setnombre(nombre);

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
                stage.initStyle(StageStyle.UNDECORATED);
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
        if (NoVacio(txtNombre.getText()) && NoVacio(txtCorreoE.getText()) && NoVacio(txtDui.getText())) {
            if (validarDui(txtDui.getText())){
                if (validarDuiNoExistente(txtDui.getText())) { // Verifica si el DUI no existe
                    if (validarLetras(txtNombre.getText())) {
                        if (validarCorreo(txtCorreoE.getText())) {
                            registrardatos();

                        } else {
                            CustomAlert customAlert = new CustomAlert();
                            customAlert.mostrarAlertaPersonalizada("Error", "Ingrese un correo válido.", (Stage) btnSiguiente.getScene().getWindow());
                            return;
                        }
                    } else {
                        CustomAlert customAlert = new CustomAlert();
                        customAlert.mostrarAlertaPersonalizada("Error", "Solo se pueden ingresar letras en el nombre.", (Stage) btnSiguiente.getScene().getWindow());
                        return;
                    }
                }else{
                    CustomAlert customAlert = new CustomAlert();
                    customAlert.mostrarAlertaPersonalizada("Error", "El DUI ya existe en la base de datos.", (Stage) btnSiguiente.getScene().getWindow());
                    return;
                }
            }else {
                CustomAlert customAlert = new CustomAlert();
                customAlert.mostrarAlertaPersonalizada("Error", "Ingrese un DUI válido.", (Stage) btnSiguiente.getScene().getWindow());
                return;
            }
        }
        else {
            CustomAlert customAlert = new CustomAlert();
            customAlert.mostrarAlertaPersonalizada("Error", "Ingresar datos, no pueden haber campos vacíos.", (Stage) btnSiguiente.getScene().getWindow());
            return;        }
    }
    private boolean validarDuiNoExistente(String dui) {
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();

        String consulta = "SELECT COUNT(*) FROM tbempleados WHERE dui = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, dui);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0; // Retorna true si el DUI no existe en la base de datos
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // En caso de error o excepción
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
