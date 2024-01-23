package com.example.ng_ingenieros.Controlador;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class AlertDos {

    public static boolean mostrarAlerta(String mensaje, String titulo) {
        try {
            // Crear un cargador FXML
            FXMLLoader loader = new FXMLLoader(AlertDos.class.getResource("/com/example/ng_ingenieros/AlertDOS.fxml"));
            Pane root = loader.load();

            // Obtener el controlador
            CustomAlertDOSController controller = loader.getController();

            // Configurar el mensaje y título
            controller.setMensaje(mensaje);
            controller.setTitulo(titulo);

            // Crear una nueva ventana (Stage) para la alerta
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Esperar hasta que el usuario cierre la ventana

            // Obtener la respuesta del usuario
            return controller.obtenerRespuesta();

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Manejar errores
        }
    }
}
