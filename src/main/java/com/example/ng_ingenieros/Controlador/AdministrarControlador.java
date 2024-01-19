package com.example.ng_ingenieros.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class AdministrarControlador {
    @FXML
    private Button btnBancos,btnCargos, btnUsuarios ;
    public void initialize() {
        // Configura el evento de clic para el botón
        btnBancos.setOnAction(this::btnBancosOnAction);
        btnCargos.setOnAction(this::btnCargosOnAction);
        btnUsuarios.setOnAction(this::btnUsuariosOnAction);
    }
    private void btnBancosOnAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/CrudBancos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nueva");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void btnCargosOnAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/CrudCargos.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nueva");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnUsuariosOnAction(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/CrudUsuarios.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nueva");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
