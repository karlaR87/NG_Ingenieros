package com.example.ng_ingenieros;

import com.example.ng_ingenieros.Controlador.CustomAlertController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CustomAlert extends Application {

    @Override
    public void start(Stage primaryStage) {
        mostrarAlertaPersonalizada("Alerta Personalizada", "Este es un mensaje de alerta.", primaryStage);
    }

    public void mostrarAlertaPersonalizada(String titulo, String mensaje, Stage ownerStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Alert.fxml"));
            Parent root = loader.load();

            // Configuración del controlador del FXML
            CustomAlertController controller = loader.getController();
            controller.initData(titulo, mensaje);

            // Configuración del Stage para la ventana de alerta
            Stage alertStage = new Stage();
            alertStage.initModality(Modality.APPLICATION_MODAL);
            alertStage.initStyle(StageStyle.TRANSPARENT);

            // Configurar el propietario del nuevo Stage
            alertStage.initOwner(ownerStage);

            Scene alertScene = new Scene(root);
            alertScene.setFill(Color.TRANSPARENT);

            alertStage.setScene(alertScene);
            alertStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
