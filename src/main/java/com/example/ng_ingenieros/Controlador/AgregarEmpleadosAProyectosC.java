package com.example.ng_ingenieros.Controlador;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AgregarEmpleadosAProyectosC {

    @FXML
    private Button btnCancelar;
    public void initialize() {
        // Configura el evento de clic para el botón
        btnCancelar.setOnAction(this::cerrarVentana);


    }
    private void cerrarVentana(javafx.event.ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
