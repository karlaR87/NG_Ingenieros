package com.example.ng_ingenieros.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProyectosControlador {
    @FXML
    private Button btnAgregar;
    public void initialize() {
        // Configura el evento de clic para el botón
       // Stage.initStyle(StageStyle.UNDECORATED); // Esto quitará la barra de título

        btnAgregar.setOnAction(this::Abrir);
    }
    private void Abrir(ActionEvent actionEvent) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/ProyectosAgregar.fxml"));
            Parent root = loader.load();

            // Crear un nuevo Stage
            Stage stage = new Stage();
            stage.setTitle("Nueva Ventana");

            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el estilo para quitar la barra de título
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setScene(new Scene(root));
            stage.showAndWait(); // Mostrar y esperar hasta que se cierre

            // La ventana se desbloqueará una vez que se cierre la ventana secundaria
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
