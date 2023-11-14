package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegistrarseSegundoControlador {
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private PasswordField txtConfirmaContra;
    @FXML
    private TextField txtUsuario;

    @FXML
    private Button btnRegistrarse, btnRegresar;

    @FXML
    private Label lbAdvertencia;



    public void initialize() {
        // Configura el evento de clic para el botón
        btnRegistrarse.setOnAction(this::btnRegistrarseOnAction);
        btnRegresar.setOnAction(this::btnRegresarOnAction);


    }


    private void btnRegistrarseOnAction(ActionEvent event) {
        registrardatos();
    }

    private void btnRegresarOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Registrarse.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrarse");


            // Configurar la modalidad (bloquea la ventana principal)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Configurar el estilo para quitar la barra de título
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void registrardatos(){
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();

        String user = txtUsuario.getText();
        String contra = txtContraseña.getText();
        String confirmarCon = txtConfirmaContra.getText();
//hora crea un String para hacer la insercion
        String Insercion = "insert into tbusuarios(nombreUsuario, contraseña) values(?,?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, contra);
            preparedStatement.executeUpdate();

            if (txtConfirmaContra.getText().equals(txtContraseña.getText())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setTitle("Registrarse");

                    stage.setScene(new Scene(root));
                    stage.show();

                    // Opcional: Cerrar la ventana actual
                    ((Stage) txtUsuario.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                lbAdvertencia.setText("La contraseña no coincide");
            }






        } catch (Exception e){
            e.printStackTrace();


        }
    }

}


