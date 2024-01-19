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


public class AgregarBancosControlador {
    @FXML
    private TextField txtNombreBanco;
    @FXML
    private Button btnAgregarBanco, btnCancelar;
    private TableView<Bancos> tbBanco;

    @FXML
    private Pane topPane; // Asegúrate de que tienes una referencia a tu AnchorPane principal
    private double xOffset =0;
    private double yOffset =0;
    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() - event.getX();
        yOffset = stage.getY() - event.getY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() +yOffset);
    }

    public void initialize() {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::btnCancelarOnAction);
        btnAgregarBanco.setOnAction(actionEvent -> {
            try {
                BtnAgregarBancoOnAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });




    }

    private void btnCancelarOnAction(ActionEvent event){
        ((Stage) txtNombreBanco.getScene().getWindow()).close();
    }
    private void BtnAgregarBancoOnAction(ActionEvent event) throws SQLException {
        validaciones();
    }

    private void AgregarBanco() throws SQLException{
        String nombre = txtNombreBanco.getText();
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        //ahora crea un String para hacer la insercion
        String Insercion = "insert into tbBancos(banco) values(?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, nombre);
            preparedStatement.executeUpdate();

            mostrarAlerta("Alerta", "Se agrego el banco con exito");
            if (tbBanco != null) {
                tbBanco.getItems().clear();
                CrudBancosControlador crudBancosControlador = new CrudBancosControlador();
                crudBancosControlador.setTableBanco(tbBanco);
                crudBancosControlador.cargarDatos();

            }
            ((Stage) txtNombreBanco.getScene().getWindow()).close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void validaciones() throws SQLException {
        if (validarLetras(txtNombreBanco.getText())){
            AgregarBanco();
        }
        else {
            mostrarAlerta("Error de Validación", "Solo se pueden ingresar letras en el nombre.");
        }
    }
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z ]+");
    }



    public void setTableBanco(TableView<Bancos> tbBanco) {
        this.tbBanco = tbBanco;
    }
}


