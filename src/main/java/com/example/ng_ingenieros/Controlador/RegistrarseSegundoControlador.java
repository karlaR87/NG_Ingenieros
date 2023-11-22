package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    @FXML
    private ComboBox cmbNivel;


    public void initialize(){
        // Configura el evento de clic para el botón
        btnRegistrarse.setOnAction(this::btnRegistrarseOnAction);
        btnRegresar.setOnAction(this::btnRegresarOnAction);
        cargarnivelCombobox();
        cmbNivel.setPromptText("Seleccione el nivel del usuario");
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


    private void cargarnivelCombobox() {
        // Crear una lista observable para almacenar los datos
        ObservableList<String> data = FXCollections.observableArrayList();

        // Conectar a la base de datos y recuperar los datos
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();
        try {

            // Reemplaza con tu propia lógica de conexión
            String query = "SELECT idNivelUsuario, usuario FROM tbNivelesUsuario";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Recorrer los resultados y agregarlos a la lista observable
            while (resultSet.next()) {
                String item = resultSet.getString("usuario"); // Reemplaza con el nombre de la columna de tu tabla
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }

        // Asignar los datos al ComboBox
        cmbNivel.setItems(data);
    }



}


