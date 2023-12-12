package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.HelloApplication;
import com.example.ng_ingenieros.Validaciones;
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
    private TextField txtNombreBanco;
    @FXML
    private Button btnAgregarBanco, btnCancelarCargo;

    public void initialize() {
        // Configura el evento de clic para el botón
        btnCancelarCargo.setOnAction(this::cerrarVentana);

    }

    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
