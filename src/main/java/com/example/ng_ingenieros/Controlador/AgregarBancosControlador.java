package com.example.ng_ingenieros.Controlador;


import com.example.ng_ingenieros.*;
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


public class AgregarBancosControlador {
    @FXML
    private TextField txtNombreBanco;
    @FXML
    private Button btnAgregarBanco, btnCancelar;

    private TableView<Bancos> tbBanco;

    public void initialize() {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::btnCancelarOnAction);
        btnAgregarBanco.setOnAction(this::BtnAgregarBancoOnAction);


    }
    public void setTableBanco(TableView<Bancos> tbBanco) {
        this.tbBanco = tbBanco;
    }


    private void btnCancelarOnAction(ActionEvent event){
        ((Stage) txtNombreBanco.getScene().getWindow()).close();
    }
    private void BtnAgregarBancoOnAction(ActionEvent event){
        AgregarBanco();
    }

    private void AgregarBanco(){
        String nombre = txtNombreBanco.getText();
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();

        //ahora crea un String para hacer la insercion
        String Insercion = "insert into tbBancos(banco) values(?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, nombre);
            preparedStatement.executeUpdate();
                /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Registrarse");

                stage.setScene(new Scene(root));
                stage.show();*/

            // Opcional: Actualizar la tabla en tiempo real

            mostrarAlerta("Alerta", "Se agrego el banco con exito");
            // Opcional: Actualizar la tabla en tiempo real
            if (tbBanco != null) {
                tbBanco.getItems().clear();
                CrudBancosControlador crudBancosControlador = new CrudBancosControlador();
                crudBancosControlador.setTableBanco(tbBanco);
                crudBancosControlador.cargarDatos();
            }


            // Opcional: Cerrar la ventana actual
                ((Stage) txtNombreBanco.getScene().getWindow()).close();


        } catch (Exception e){
            e.printStackTrace();
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


