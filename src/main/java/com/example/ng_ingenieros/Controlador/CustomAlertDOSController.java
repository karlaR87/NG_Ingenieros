package com.example.ng_ingenieros.Controlador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
public class CustomAlertDOSController {
    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Label lbTitulo;

    @FXML
    private Label lbMensajes;

    private boolean respuesta; // Para almacenar la respuesta (true: Sí, false: No)

    public void initialize() {
        // Puedes configurar cosas aquí cuando se inicia el controlador, si es necesario
    }

    public void setMensaje(String mensaje) {
        lbMensajes.setText(mensaje);
    }

    public void setTitulo(String titulo) {
        lbTitulo.setText(titulo);
    }

    @FXML
    private void handleAceptar(ActionEvent event) {
        respuesta = true; // Establecer la respuesta a "Sí"
        cerrarVentana();
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        respuesta = false; // Establecer la respuesta a "No"
        cerrarVentana();
    }

    public boolean obtenerRespuesta() {
        return respuesta;
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
