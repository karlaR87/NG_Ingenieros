package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.PanelesProyectos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import javafx.scene.control.Button;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenuController implements Initializable {


    @FXML
    private BorderPane NuevoIdParaElCentro;




    @FXML
    private void abrirInicio(ActionEvent event) {
        loadWindow("/com/example/ng_ingenieros/MostrarPaneles.fxml");



    }



    @FXML
    private void abrirProyectos(ActionEvent event) {


        loadWindow("/com/example/ng_ingenieros/Proyectos.fxml");
    }

    @FXML
    private void abrirEmpleados(ActionEvent event) {
        // Cambiar el color de fondo y el color del texto al hacer clic en el botón "Empleados"


        loadWindow("/com/example/ng_ingenieros/Empleados.fxml");


    }

    private void loadWindow(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            NuevoIdParaElCentro.setCenter(root);  // Establecer el root en el centro del BorderPane

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }


}
