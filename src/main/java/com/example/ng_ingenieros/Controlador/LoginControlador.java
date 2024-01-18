package com.example.ng_ingenieros.Controlador;

import com.example.ng_ingenieros.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginControlador {

    @FXML
    private Button btnRegistrar;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private Button btnIngresar;
    @FXML
    private Label lbmensaje;
    @FXML
    private Button btnContraseña;

    @FXML
    private Button btnClose;

    @FXML
    private Pane topPane; // Asegúrate de que tienes una referencia a tu AnchorPane principal
    private double xOffset =0;
    private double yOffset =0;
    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() - event.getX();
        yOffset = stage.getY() - event.getY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    protected void HandleCloseAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }



    public void initialize() {
        // Configura el evento de clic para el botón
        btnIngresar.setOnAction(this::btnIngresarOnAction);
        btnRegistrar.setOnAction(this::btnRegistrarOnAction);
        btnContraseña.setOnAction(this::btnContraseñaOnAction);
    }

    private void btnContraseñaOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/RecuperacionContraseña.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            // Cierras la ventana actual
            Stage currentStage = (Stage) btnClose.getScene().getWindow();
            currentStage.close();

            // Muestras la nueva ventana
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para abrir una nueva ventana
    private void btnIngresarOnAction(ActionEvent event) {

        if (txtUsuario.getText().isBlank() == false && txtContraseña.getText().isBlank() == false) {
            validatelogin();
        }

    }

    private void btnRegistrarOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/Registrarse.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            ((Stage) txtUsuario.getScene().getWindow()).close();
            stage.show();


            Node source = (Node) event.getSource();
            stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /*private void loadWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/MenuPrincipal.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Inicio");

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private static String contraseñaEncriptada;

    public void contraseña() {
        String contraseña = txtContraseña.getText();
        contraseñaEncriptada = encriptarContraseña(contraseña);
        System.out.println("Contraseña original: " + contraseña);
        System.out.println("Contraseña encriptada: " + contraseñaEncriptada);
    }

    public static String encriptarContraseña(String contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseña.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexHash = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void validatelogin() {
        Conexion conexion = new Conexion();
        Connection connection = conexion.obtenerConexion();

        String verifylogin = "SELECT COUNT(1) FROM tbusuarios WHERE nombreUsuario = ? AND contraseña = ?";
        String nivel = "SELECT idNivelUsuario FROM tbusuarios WHERE nombreUsuario = ? AND contraseña = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(verifylogin);
            PreparedStatement preparedStatements = connection.prepareStatement(nivel);
            preparedStatement.setString(1, txtUsuario.getText());
            preparedStatement.setString(2, contraseñaEncriptada);
            ResultSet queryResult = preparedStatement.executeQuery();            //---------------------
            preparedStatements.setString(1, txtUsuario.getText());
            preparedStatements.setString(2, contraseñaEncriptada);
            ResultSet niveles = preparedStatements.executeQuery();

            contraseña();
            while (queryResult.next()) {
                  if (queryResult.getInt(1) == 1){
                      while (niveles.next()) {
                          if (niveles.getInt(1) == 2) {
                              try {
                                  FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/MenuPrincipal.fxml"));
                                  Parent root = loader.load();
                                  MenuController controller = loader.getController();

                                  controller.btnOtros.setVisible(true);

                                  Stage stage = new Stage();
                                  stage.setTitle("Registrarse");


                                  stage.setScene(new Scene(root));
                                  ((Stage) txtUsuario.getScene().getWindow()).close();
                                  stage.show();

                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          } else if (niveles.getInt(1) == 1) {
                              FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ng_ingenieros/MenuPrincipal.fxml"));
                              Parent root = loader.load();
                              MenuController controller = loader.getController();

                              controller.btnOtros.setVisible(false);

                              Stage stage = new Stage();
                              stage.setTitle("Registrarse");


                              stage.setScene(new Scene(root));
                              ((Stage) txtUsuario.getScene().getWindow()).close();
                              stage.show();
                          }
                      }

                  }
                  else{
                      mostrarAlerta("Error", "Las contraseña o el usuario es incorrecto");
                  }


            }


        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }





   /* public void btnIngresarOnAction(ActionEvent event) {
        String user = txtUsuario.getText();
        String contra = txtContraseña.getText();
     //   loadWindow("/com/example/ng_ingenieros/MenuPrincipal.fxml");
        if (user == "elmer" && contra == "hola123") {
            loadWindow("/com/example/ng_ingenieros/MenuPrincipal.fxml");

        }*/

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}