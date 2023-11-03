package com.example.ng_ingenieros.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private BorderPane NuevoIdParaElCentro;


    @FXML
    private void abrirInicio(ActionEvent event) {
        loadWindow("/com/example/ng_ingenieros/Incio.fxml");
    }

    @FXML
    private void abrirProyectos(ActionEvent event) {
        loadWindow("/com/example/ng_ingenieros/Proyectos.fxml");
    }

    @FXML
    private void abrirEmpleados(ActionEvent event) {
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
        // Cargar la ventana "Inicio" en el panel central al iniciar la aplicación
        loadWindow("/com/example/ng_ingenieros/Incio.fxml");
    }
}
