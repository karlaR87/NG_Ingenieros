package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

public class AgregarCargosControlador {

    @FXML
    private TextField txtNombrecargo;
    @FXML
    private Button btnAgregarCargos, btnCancelarCargo;

    private TableView<Cargos> tbCargo;

    public void initialize() {
        // Configura el evento de clic para el botón
        btnCancelarCargo.setOnAction(this::btnCancelarOnAction);
        btnAgregarCargos.setOnAction(this::BtnAgregarCargosOnAction);

    }

    public void setTableCargos(TableView<Cargos> tbCargo) {
        this.tbCargo = tbCargo;
    }

    private void btnCancelarOnAction(ActionEvent event){
        ((Stage) txtNombrecargo.getScene().getWindow()).close();
    }

    private void BtnAgregarCargosOnAction(ActionEvent event){
        validaciones();
    }



    public void agregarCargos(){
        String cargo = txtNombrecargo.getText();
        Conexion conexion = new Conexion();
        Connection conn = conexion.obtenerConexion();

        String sql = "INSERT INTO tbcargos (cargo) VALUES (?)";
        try  {

            PreparedStatement ps =conn.prepareStatement(sql);
            ps.setString(1,cargo);
            ps.executeUpdate();




            agregar_empleadosControlador.mostrarAlerta("Inserción de datos", "los datos han sido agregados exitosamente", Alert.AlertType.INFORMATION);
            if ( tbCargo != null) {
                tbCargo.getItems().clear();
                CrudCargosControlador crudCargosControlador = new CrudCargosControlador();
                crudCargosControlador.setTableCargos(tbCargo);
                crudCargosControlador.cargarDatos();
            }

            // Opcional: Cerrar la ventana actual
            ((Stage) txtNombrecargo.getScene().getWindow()).close();





        }catch (SQLException e) {
            agregar_empleadosControlador.mostrarAlerta("Error", "Ha ocurrido un error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    public void validaciones(){
        if (validarLetras(txtNombrecargo.getText())){
            agregarCargos();
        }
        else {
            mostrarAlerta("Error de Validación", "Solo se pueden ingresar letras en el nombre.");
        }
    }
    public static boolean validarLetras(String input) {
        return input.matches("[a-zA-Z ]+");
    }
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


