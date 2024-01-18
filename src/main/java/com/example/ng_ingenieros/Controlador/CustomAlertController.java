package com.example.ng_ingenieros.Controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CustomAlertController {

    @FXML
    private Pane customAlertPane;

    @FXML
    private Label lbTitulo;

    @FXML
    private Label lbMensaje;

    @FXML
    private Button btnAceptar;

    public void initData(String titulo, String mensaje) {
        lbTitulo.setText(titulo);
        lbMensaje.setText(mensaje);
        btnAceptar.setOnAction(event -> cerrarVentana());

    }

    private void cerrarVentana() {
        // Obtener la referencia al Stage actual y cerrarlo
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }

    // Puedes agregar más métodos según sea necesario
}
