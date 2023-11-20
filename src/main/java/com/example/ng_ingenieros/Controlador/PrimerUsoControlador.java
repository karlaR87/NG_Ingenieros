package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import com.example.ng_ingenieros.HelloApplication;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class PrimerUsoControlador {
    @FXML
    private PasswordField txtContraseña, txtConfirmarContraseña;

    @FXML
    private TextField txtUser, txtCorreo;

    @FXML
    private Button btnAceptar;

    @FXML
    private Label lbAdvertencia;

    public void initialize() {
        // Configura el evento de clic para el botón
        btnAceptar.setOnAction(this::btnAceptarOnAction);
    }



    // Método para abrir una nueva ventana
    private void btnAceptarOnAction(ActionEvent event) {
        registrardatos();
    }



    public void registrardatos(){
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();

        String user = txtUser.getText();
        String correo = txtCorreo.getText();
        String contra = txtContraseña.getText();
        String confirmarCon = txtConfirmarContraseña.getText();

          //ahora crea un String para hacer la insercion
        String Insercion = "insert into tbusuarios(nombreUsuario, contraseña, correo, idNivelUsuario) values(?,?,?,2);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Insercion);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, contra);
            preparedStatement.setString(3, correo);
            preparedStatement.executeUpdate();

            if (txtConfirmarContraseña.getText().equals(txtContraseña.getText())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Login.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setTitle("Login");

                    stage.setScene(new Scene(root));
                    stage.show();

                    // Opcional: Cerrar la ventana actual
                    ((Stage) txtUser.getScene().getWindow()).close();
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
