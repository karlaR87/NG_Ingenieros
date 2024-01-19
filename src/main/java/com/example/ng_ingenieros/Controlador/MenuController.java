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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javafx.scene.control.Button;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
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
    public Button btnOtros;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadWindow("/com/example/ng_ingenieros/MostrarPaneles.fxml");

    }

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

        loadWindow("/com/example/ng_ingenieros/empleados.fxml");
    }


    @FXML
    private void abrirAdministrar(ActionEvent event) {
        loadWindow("/com/example/ng_ingenieros/Administrar.fxml");
    }
    @FXML
    private void atras(ActionEvent actionEvent) {
        try {
            // Obtener la referencia al Stage actual (ventana)
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Cargar el archivo FXML del login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
            Parent root = loader.load();

            // Crear un nuevo Stage para la ventana de login
            Stage loginStage = new Stage();
            loginStage.setTitle("Nueva Ventana");

            // Configurar el estilo para quitar la barra de título
            loginStage.initStyle(StageStyle.UNDECORATED);

            loginStage.setScene(new Scene(root));
            loginStage.show();

            // Cerrar la ventana actual
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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



}
