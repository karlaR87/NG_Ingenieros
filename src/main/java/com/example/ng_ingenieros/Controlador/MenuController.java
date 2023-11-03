package com.example.ng_ingenieros.Controlador;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class MenuController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private AnchorPane pane;
    @FXML
    private void abrirInicio(){
        cargarVentana("Inicio.fxml");
    }
    @FXML
    private void abrirProyectos(){
        cargarVentana("Proyectos.fxml");
    }

    @FXML
    private void abrirEmpleados(){
        cargarVentana("empleados.fxml");
    }

        private void cargarVentana(String fxmlFile) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                AnchorPane ventana = loader.load();

                // Obtener el controlador de la ventana
                Initializable controller = loader.getController();

                // Limpiar el panel central y agregar la nueva ventana
                pane.getChildren().clear();
                pane.getChildren().add(ventana);
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de errores
            }
        }




}
















